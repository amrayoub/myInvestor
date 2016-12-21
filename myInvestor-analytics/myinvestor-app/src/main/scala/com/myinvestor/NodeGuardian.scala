package com.myinvestor

import akka.actor.{Actor, Props}
import org.apache.spark.streaming.StreamingContext

/** A `NodeGuardian` manages the worker actors at the root of each MyInvestor
  * deployed application, where any special application logic is handled in the
  * implementer here, but the cluster work, node lifecycle and supervision events
  * are handled in [[ClusterAwareNodeGuardian]], in 'myinvestor/myinvestor-core.
  *
  * This `NodeGuardian` creates the [[KafkaStreamingActor]] which creates a streaming
  * pipeline from Kafka to Cassandra, via Spark, which streams the raw data from Kafka,
  * transforms data to [[com.myinvestor.Trading.RawTradingData]],
  * and saves the new data to the cassandra raw data table on arrival.
  */
class NodeGuardian(ssc: StreamingContext, kafkaConfig: Map[String, String], settings: MyInvestorSettings) extends ClusterAwareNodeGuardian with AggregationActor {

  import TradingEvent._
  import settings._

  /** Creates the Kafka stream saving raw data and aggregated data to cassandra. */
  context.actorOf(Props(new KafkaStreamingActor(kafka.kafkaParams, ssc, settings, self)), "kafka-stream")

  /** The Spark/Cassandra computation actors: For the tutorial we just use 2005 for now. */
  val temperature = context.actorOf(Props(new TemperatureActor(ssc.sparkContext, settings)), "temperature")
  val precipitation = context.actorOf(Props(new PrecipitationActor(ssc, settings)), "precipitation")
  val station = context.actorOf(Props(new WeatherStationActor(ssc.sparkContext, settings)), "weather-station")

  override def preStart(): Unit = {
    super.preStart()
    cluster.joinSeedNodes(Vector(cluster.selfAddress))
  }

  /** When [[OutputStreamInitialized]] is received in the parent actor, [[ClusterAwareNodeGuardian]],
    * from the [[KafkaStreamingActor]] after it creates and defines the [[KafkaInputDStream]],
    * the Spark Streaming checkpoint can be set, the [[StreamingContext]] can be started, and the
    * node guardian actor moves from [[uninitialized]] to [[initialized]]with [[akka.actor.ActorContext.become()]].
    *
    * @see [[ClusterAwareNodeGuardian]]
    */
  override def initialize(): Unit = {
    super.initialize()
    ssc.checkpoint(SparkCheckpointDir)
    ssc.start() // currently can not add more dstreams once started

    context become initialized
  }

  /** This node guardian's customer behavior once initialized. */
  def initialized: Actor.Receive = {
    case e: TemperatureRequest => temperature forward e
    case e: PrecipitationRequest => precipitation forward e
    case e: WeatherStationRequest => station forward e
    case GracefulShutdown => gracefulShutdown(sender())
  }

}

