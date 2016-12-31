package com.myinvestor

import akka.japi.Util.immutableSeq
import com.typesafe.config.{Config, ConfigFactory}
import akka.japi.Util.immutableSeq
import org.apache.spark.internal.config

import scala.collection.immutable

/**
  * Client configuration settings
  */
final class ClientSettings (conf: Option[Config] = None) extends Serializable {

  val rootConfig: Config = conf match {
    case Some(c) => c.withFallback(ConfigFactory.load())
    case _ => ConfigFactory.load
  }

  protected val akka: Config = rootConfig.getConfig("akka")
  protected val kafka: Config = ConfigFactory.load.getConfig("kafka")
  protected val myInvestor: Config = rootConfig.getConfig("myInvestor")


  val KafkaHosts = immutableSeq(kafka.getStringList("hosts")).toSet
  val KafkaTopic: String = kafka.getString("topic.source")
  val KafkaKey: String = kafka.getString("group.id")
  val KafkaBatchSendSize: Int = kafka.getInt("batch.send.size")

}