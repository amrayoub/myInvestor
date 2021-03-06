package com.myinvestor.actor

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.datastax.spark.connector.streaming._
import com.myinvestor.AppSettings
import com.myinvestor.Trade.JsonApiSupport._
import com.myinvestor.TradeEvent.OutputStreamInitialized
import com.myinvestor.TradeSchema._
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import spray.json._

/**
  * The KafkaStreamActor creates a streaming pipeline from Kafka to Cassandra via Spark.
  * It creates the Kafka stream which streams the source data, transforms it, to
  * a column entry for a specific stock trading, and saves the new data
  * to the cassandra table as it arrives.
  */
class ExchangeActor(kafkaParams: Map[String, Object],
                    ssc: StreamingContext,
                    settings: AppSettings,
                    listener: ActorRef) extends AggregationActor with ActorLogging {

  import settings._

  val topics = Array(KafkaTopicExchange)
  val kafkaStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topics, kafkaParams))

  // For debugging
  kafkaStream.map(_.value.parseJson).map(_.convertTo[Exchange]).saveToCassandra(Keyspace, ExchangeTable)

  // Convert the JSON string back to an object
  //kafkaStream.map {
  //  value =>
  //}.saveToCassandra(Keyspace, ExchangeTable)

  // Notifies the supervisor that the Spark Streams have been created and defined.
  // Now the [[StreamingContext]] can be started.
  listener ! OutputStreamInitialized

  def receive: Actor.Receive = {
    case e => // ignore
  }
}
