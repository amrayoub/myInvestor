import sbt._

/**
  * Module dependencies.
  */
object Dependencies {

  import Versions._

  object Library {

    val technicalAnalysis = "eu.verdelhan" % "ta4j"% TAVersion    
    val logback = "ch.qos.logback" % "logback-classic" % Logback
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % ScalaLogging % "provided"
    val scalaLoggingSlf4j = "com.typesafe.scala-logging" %% "scala-logging-slf4j" %  ScalaLoggingSlf4j
    val scalaConfig = "com.typesafe" % "config" % ScalaConfig
    val jodaTime = "joda-time" % "joda-time" % JodaTime
    val sparkCassandraConnector = "com.datastax.spark" %% "spark-cassandra-connector" % SparkCassandra % "provided"
    val sparkCore = "org.apache.spark" %% "spark-core" % Spark  % "provided"
    val sparkSql = "org.apache.spark" %% "spark-sql" % Spark  % "provided"
    val sparkStreaming = "org.apache.spark" %% "spark-streaming" % Spark  % "provided"
    val sparkGraphx = "org.apache.spark" %% "spark-graphx" % Spark  % "provided"

  }

  import Library._

  val spark = Seq(sparkCassandraConnector, sparkCore, sparkSql, sparkStreaming, sparkGraphx)

  val logging = Seq(logback, scalaLogging, scalaLoggingSlf4j)

  val ta = Seq(technicalAnalysis)

  val time = Seq(jodaTime)

  val config = Seq(scalaConfig)

  // Module dependencies
  val core = time ++  config ++ logging 

  val client = spark ++ ta
  
  val app = spark ++ ta

  val example = spark ++ ta

}

