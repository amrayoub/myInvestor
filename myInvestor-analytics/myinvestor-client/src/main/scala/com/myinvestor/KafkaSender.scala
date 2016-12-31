package com.myinvestor

import java.util.Properties

import akka.actor.{Actor, ActorLogging}
import kafka.serializer.StringEncoder
import kafka.server.KafkaConfig
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}

/**
  * Simple producer for an Akka Actor using string encoder and default partitioner.
  **/
abstract class KafkaSenderActor[K, V] extends Actor with ActorLogging {

  import KafkaEvent._

  def config: Properties

  private val producer = new KafkaSender[K, V](config)

  override def postStop(): Unit = {
    log.info("Shutting down producer.")
    producer.close()
  }

  def receive = {
    case e: KafkaMessageEnvelope[K, V] => producer.send(e)
  }
}

// Simple producer using string encoder and default partitioner.
class KafkaSender[K, V](config: Properties) {

  def this(brokers: Set[String], batchSize: Int, serializerFqcn: String) =
    this(KafkaSender.createConfig(brokers, batchSize, serializerFqcn))

  def this(config: KafkaConfig) =
    this(KafkaSender.defaultConfig(config))

  import KafkaEvent._

  private val producer = new KafkaProducer[K, V](config)

  /** Sends the data, partitioned by key to the topic. */
  def send(e: KafkaMessageEnvelope[K, V]): Unit =
    batchSend(e.topic, e.key, e.messages)

  /* Sends a single message. */
  def send(topic: String, key: K, message: V): Unit =
    batchSend(topic, key, Seq(message))

  def batchSend(topic: String, key: K, batch: Seq[V]): Unit = {
    for (message <- batch) {
      producer.send(new ProducerRecord[K, V](topic, key, message))
    }
  }

  def close(): Unit = producer.close()

}

object KafkaEvent {
  case class KafkaMessageEnvelope[K, V](topic: String, key: K, messages: V*)
}

object KafkaSender {

  def createConfig(brokers: Set[String], batchSize: Int, serializerFqcn: String): Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers.mkString(","))
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serializerFqcn)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializerFqcn)
    props.put(ProducerConfig.ACKS_CONFIG, "1")
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize.toString)
    props
  }

  def defaultConfig(config: KafkaConfig): Properties =
    createConfig(Set(s"${config.hostName}:${config.port}"), 100, classOf[StringEncoder].getName)
}