package com.myinvestor

import java.util.Properties

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props}
import akka.routing.BalancingPool
import com.myinvestor.KafkaEvent.KafkaMessageEnvelope
import com.myinvestor.cluster.ClusterAwareNodeGuardian
import com.typesafe.config.ConfigFactory
import kafka.serializer.StringEncoder

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
  val settings = new ClientSettings
  import settings._

  cluster.joinSeedNodes(Vector(cluster.selfAddress))

  // The [[KafkaPublisherActor]] as a load-balancing pool router
  // which sends messages to idle or less busy routees to handle work.
  val router = context.actorOf(BalancingPool(5).props(Props(new KafkaPublisherActor(KafkaHosts, KafkaBatchSendSize))), "kafka-ingestion-router")

  // Wait for this node's [[akka.cluster.MemberStatus]] to be
  // [[MemberUp]] before starting work, which means
  // it's membership in the [[Cluster]] node ring has been gossipped, and we
  // can leverage the cluster's adaptive load balancing which will route data
  // to the `MyInvestorApp` nodes based on most healthy, by their health metrics
  // - cpu, system load average and heap.
  cluster registerOnMemberUp {

    // As http data is received, publishes to Kafka.
    context.actorOf(BalancingPool(10).props(Props(new GoogleFinanceDataFeedActor(router))), "dynamic-data-feed")

    log.info("Starting data ingestion on {}.", cluster.selfAddress)

    // Handles initial data ingestion in Kafka for running as a demo.
    //for (fs <- initialData; data <- fs.data) {
    //  log.info("Sending {} to Kafka", data)
    //  router ! KafkaMessageEnvelope[String, String](KafkaTopic, KafkaKey, data)
    //}
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
  def this(hosts: Set[String], batchSize: Int) = this(KafkaSender.createConfig(hosts, batchSize, classOf[StringEncoder].getName))
}

class GoogleFinanceDataFeedActor(kafka: ActorRef) extends Actor with ActorLogging {
  implicit val system = context.system
  val settings = new ClientSettings
  import settings._

  kafka ! KafkaMessageEnvelope[String, String](KafkaTopic, KafkaKey, "client testing")

  def receive: Actor.Receive = {
    case e =>
  }
}