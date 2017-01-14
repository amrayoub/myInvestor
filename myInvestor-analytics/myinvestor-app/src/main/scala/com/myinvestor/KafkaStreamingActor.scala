package com.myinvestor

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.myinvestor.TradeEvent.OutputStreamInitialized
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._


/**
  * The KafkaStreamActor creates a streaming pipeline from Kafka to Cassandra via Spark.
  * It creates the Kafka stream which streams the source data, transforms it, to
  * a column entry for a specific stock trading, and saves the new data
  * to the cassandra table as it arrives.
  */
class KafkaStreamingActor(kafkaParams: Map[String, Object],
                          ssc: StreamingContext,
                          settings: AppSettings,
                          listener: ActorRef) extends AggregationActor with ActorLogging {

  import settings._

  // TODO
  val topics = Array(KafkaTopicExchange)
  val kafkaStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topics, kafkaParams))

  // For debugging
  kafkaStream.map(record => record.value.toString).print

  // Notifies the supervisor that the Spark Streams have been created and defined.
  // Now the [[StreamingContext]] can be started.
  listener ! OutputStreamInitialized

  def receive: Actor.Receive = {
    case e => // ignore
  }
}
