package com.myinvestor

import java.net.InetAddress

import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

/**
  * Application settings. First attempts to acquire from the deploy environment.
  * If not exists, then from -D java system properties, else a default config.
  *
  * Settings in the environment such as: SPARK_HA_MASTER=local[10] is picked up first.
  *
  * Settings from the command line in -D will override settings in the deploy environment.
  * For example: sbt -Dspark.master="local[12]" run
  *
  * If you have not yet used Typesafe Config before, you can pass in overrides like so:
  *
  * {{{
  *   new Settings(ConfigFactory.parseString("""
  *      spark.master = "some.ip"
  *   """))
  * }}}
  *
  * Any of these can also be overriden by your own application.conf.
  *
  * @param conf Optional config for test
  */
final class MyInvestorSettings(conf: Option[Config] = None) extends Serializable {

  val localAddress = InetAddress.getLocalHost.getHostAddress

  val rootConfig = conf match {
    case Some(c) => c.withFallback(ConfigFactory.load())
    case _ => ConfigFactory.load
  }

  protected val spark = rootConfig.getConfig("spark")
  protected val cassandra = rootConfig.getConfig("cassandra")
  protected val kafka = ConfigFactory.load.getConfig("kafka")
  protected val myInvestor = rootConfig.getConfig("myInvestor")


  // Spark settings


  // Cassandra settings


  // Kakfa settings


  // Application settings
  val AppName = myInvestor.getString("app-name")


  /**
    * Attempts to acquire from environment, then java system properties.
    *
    * @param env
    * @param key
    * @tparam T
    * @return
    */
  def withFallback[T](env: Try[T], key: String): Option[T] = env match {
    case null => None
    case value => value.toOption
  }
}
