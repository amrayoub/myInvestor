package com.myinvestor

import akka.japi.Util.immutableSeq
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Client configuration settings
  */
final class ClientSettings(conf: Option[Config] = None) extends Serializable {

  val rootConfig: Config = conf match {
    case Some(c) => c.withFallback(ConfigFactory.load())
    case _ => ConfigFactory.load
  }

  protected val akka: Config = rootConfig.getConfig("akka")
  protected val kafka: Config = rootConfig.getConfig("kafka")
  protected val myInvestor: Config = rootConfig.getConfig("myInvestor")


  val KafkaHosts: Set[String] = immutableSeq(kafka.getStringList("hosts")).toSet
  val KafkaTopicExchange: String = kafka.getString("topic.exchange")
  val KafkaKey: String = kafka.getString("group.id")
  val KafkaBatchSendSize: Int = kafka.getInt("batch.send.size")

  val HttpHostName: String = myInvestor.getString("http.host")
  val HttpListenPort: Int = myInvestor.getInt("http.port")

}