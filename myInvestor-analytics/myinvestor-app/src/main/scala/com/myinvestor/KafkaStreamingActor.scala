package com.myinvestor

import akka.actor.{Actor, ActorLogging, ActorRef}
import kafka.serializer.StringDecoder
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.KafkaUtils

/**
  * The KafkaStreamActor creates a streaming pipeline from Kafka to Cassandra via Spark.
  * It creates the Kafka stream which streams the raw data, transforms it, to
  * a column entry for a specific stock trading, and saves the new data
  * to the cassandra table as it arrives.
  */
class KafkaStreamingActor(kafkaParams: Map[String, String],
                          ssc: StreamingContext,
                          settings: MyInvestorSettings,
                          listener: ActorRef) extends AggregationActor with ActorLogging {

  import Trading._
  import settings._

  // TODO

  val kafkaStream = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, Map(KafkaTopicRaw -> 1), StorageLevel.DISK_ONLY_2)
    .map(_._2.split(","))
    .map(RawTradingData(_))

  def receive: Actor.Receive = {
    case e => // ignore
  }
}
