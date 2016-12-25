package com.myinvestor

import akka.actor.{Actor, ActorLogging, ActorRef}
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.ConsumerStrategies._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent


/**
  * The KafkaStreamActor creates a streaming pipeline from Kafka to Cassandra via Spark.
  * It creates the Kafka stream which streams the raw data, transforms it, to
  * a column entry for a specific stock trading, and saves the new data
  * to the cassandra table as it arrives.
  */
class KafkaStreamingActor(kafkaParams: Map[String, Object],
                          ssc: StreamingContext,
                          settings: MyInvestorSettings,
                          listener: ActorRef) extends AggregationActor with ActorLogging {

  import settings._

  // TODO

  val stream = KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](KafkaTopicRaw, kafkaParams)
  )

  def receive: Actor.Receive = {
    case e => // ignore
  }
}
