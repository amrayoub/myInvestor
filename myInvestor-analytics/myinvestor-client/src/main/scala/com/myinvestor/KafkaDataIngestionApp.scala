package com.myinvestor

import java.util.Properties

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props, Terminated}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{HttpEntity, _}
import akka.http.scaladsl.server.Directives.{complete, _}
import akka.routing.BalancingPool
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.Timeout
import com.myinvestor.KafkaEvent.KafkaMessageEnvelope
import com.myinvestor.cluster.ClusterAwareNodeGuardian
import com.typesafe.config.ConfigFactory
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Run with: sbt clients/run for automatic data file import to Kafka.
  *
  */
object KafkaDataIngestionApp extends App {

  // Creates the ActorSystem.
  val system = ActorSystem("myInvestor", ConfigFactory.parseString("akka.remote.netty.tcp.port = 2551"))

  // The root supervisor and fault tolerance handler of the data ingestion nodes.
  val guardian = system.actorOf(Props[HttpNodeGuardian], "node-guardian")

  system.registerOnTermination {
    guardian ! PoisonPill
  }
}

/**
  * Ingest data from web.
  */
final class HttpNodeGuardian extends ClusterAwareNodeGuardian {
  val actorName = "kafka-ingestion-router"
  val settings = new ClientSettings

  import settings._

  cluster.joinSeedNodes(Vector(cluster.selfAddress))

  // The [[KafkaPublisherActor]] as a load-balancing pool router
  // which sends messages to idle or less busy routees to handle work.
  val router = context.actorOf(BalancingPool(5).props(Props(new KafkaPublisherActor(KafkaHosts, KafkaBatchSendSize))), actorName)

  // Wait for this node's [[MemberStatus]] to be
  // [[MemberUp]] before starting work, which means
  // it's membership in the [[Cluster]] node ring has been gossipped, and we
  // can leverage the cluster's adaptive load balancing which will route data
  // to the `MyInvestorApp` nodes based on most healthy, by their health metrics
  // - cpu, system load average and heap.
  cluster registerOnMemberUp {

    // As http data is received, publishes to Kafka.
    context.actorOf(BalancingPool(1).props(Props(new HttpDataFeedActor(router))), "dynamic-data-feed")

    log.info("Started data ingestion on {}.", cluster.selfAddress)

    router ! KafkaMessageEnvelope[String, String](KafkaTopic, KafkaKey, "client testing123")

  }

  def initialized: Actor.Receive = {
    case TradeEvent.TaskCompleted => // ignore for now
  }
}

/**
  * The KafkaPublisherActor receives initial data on startup
  * (because this is for a runnable demo) and also receives data in runtime.
  *
  */
class KafkaPublisherActor(val config: Properties) extends KafkaSenderActor[String, String] {
  def this(hosts: Set[String], batchSize: Int) = this(KafkaDataProducer.createConfig(hosts, batchSize, classOf[StringSerializer].getName))
}

class HttpDataFeedActor(kafka: ActorRef) extends Actor with ActorLogging {
  val settings = new ClientSettings

  import settings._

  implicit val system = context.system
  implicit val askTimeout: Timeout = 500.millis
  implicit val materializer = ActorMaterializer(ActorMaterializerSettings(system))
  implicit val executionContext = system.dispatcher

  // Watch for termination of the Kafka router
  context.watch(kafka)

  val route =
    path("") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<html><head><title>HTTP data feed</title></head<body><h1>HTTP data feed</h1></body></html>"))
      }
    }
  path("exchange") {
    post {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<html><head><title>HTTP data feed</title></head<body><h1>HTTP data feed</h1></body></html>"))
    }
  }

  path("stock/history") {
    post {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<html><head><title>HTTP data feed</title></head<body><h1>HTTP data feed</h1></body></html>"))
    }
  }
  path("stock/info") {
    post {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<html><head><title>HTTP data feed</title></head<body><h1>HTTP data feed</h1></body></html>"))
    }
  }
  path("stock") {
    post {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<html><head><title>HTTP data feed</title></head<body><h1>HTTP data feed</h1></body></html>"))
    }
  }

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(route, HttpHostName, HttpListenPort)

  def receive: Actor.Receive = {
    case Terminated(kafka) => bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
    case e =>
  }
}



