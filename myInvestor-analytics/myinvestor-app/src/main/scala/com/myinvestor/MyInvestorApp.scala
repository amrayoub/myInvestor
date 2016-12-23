package com.myinvestor

import java.util.concurrent.atomic.AtomicBoolean

import akka.actor.{ActorSystem, Address, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider, Props}
import akka.cluster.Cluster
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Milliseconds, StreamingContext}

import scala.concurrent.{Await, Future}

/**
  * Main application to start Kafka, ZooKeeper, Akka.
  *
  * Can be run with sbt: sbt app/run
  */
object MyInvestorApp extends App {
  val settings = new MyInvestorSettings
  import settings._

  // Create the Actor system
  val system = ActorSystem(AppName)
  val myInvestor = MyInvestor(system)
}

object MyInvestor extends ExtensionId[MyInvestor] with ExtensionIdProvider {
  override def lookup: ExtensionId[_ <: Extension] = MyInvestor
  override def createExtension(system: ExtendedActorSystem) = new MyInvestor(system)
}

class MyInvestor(system: ExtendedActorSystem) extends Extension {

  import TradingEvent.GracefulShutdown
  import system.dispatcher

  system.registerOnTermination(shutdown())

  protected val log = akka.event.Logging(system, system.name)
  protected val running = new AtomicBoolean(false)
  protected val terminated = new AtomicBoolean(false)

  val settings = new MyInvestorSettings
  import settings._

  implicit private val timeout = system.settings.CreationTimeout

  // Configures Spark
  protected val conf = new SparkConf().setAppName(getClass.getSimpleName)
                        .setMaster(SparkMaster)
                        .set("spark.cassandra.connection.host", CassandraHosts)
                        .set("spark.cleaner.ttl", SparkCleanerTtl.toString)

  // Creates the Spark Streaming context.
  protected val ssc = new StreamingContext(conf, Milliseconds(SparkStreamingBatchInterval))

  // The root supervisor and traffic controller of the app. All inbound messages go through this actor
  private val guardian = system.actorOf(Props(new NodeGuardian(ssc, kafka, settings)), "node-guardian")

  private val cluster = Cluster(system)

  val selfAddress: Address = cluster.selfAddress

  cluster.joinSeedNodes(Vector(selfAddress))

  def isRunning: Boolean = running.get
  def isTerminated: Boolean = terminated.get

  private def shutdown(): Unit = if (!isTerminated) {
    import akka.pattern.ask

    if (terminated.compareAndSet(false, true)) {
      log.info("Node {} shutting down", selfAddress)
      cluster leave selfAddress
      ssc.stop(stopSparkContext = true, stopGracefully = true)
      (guardian ? GracefulShutdown).mapTo[Future[Boolean]]
        .onComplete { _ =>
          system.terminate()
          Await.ready(system.whenTerminated, timeout.duration)
        }
    }
  }
}