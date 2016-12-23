import sbt._

/**
  * Module dependencies.
  */
object Dependencies {

  import Versions._

  object Library {

    val technicalAnalysis = "eu.verdelhan" % "ta4j" % TAVersion
    val logback = "ch.qos.logback" % "logback-classic" % Logback
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % ScalaLogging
    val scalaLoggingSlf4j = "com.typesafe.scala-logging" %% "scala-logging-slf4j" % ScalaLoggingSlf4j % "provided"
    val scalaConfig = "com.typesafe" % "config" % ScalaConfig
    val jodaTime = "joda-time" % "joda-time" % JodaTime
    val sparkCassandraConnector = "com.datastax.spark" %% "spark-cassandra-connector" % SparkCassandra % "provided"
    val sparkCore = "org.apache.spark" %% "spark-core" % Spark % "provided"
    val sparkSql = "org.apache.spark" %% "spark-sql" % Spark % "provided"
    val sparkStreaming = "org.apache.spark" %% "spark-streaming" % Spark % "provided"
    val sparkStreamingKafka = "org.apache.spark" %% "spark-streaming-kafka-0-10" % Spark % "provided"
    val sparkGraphx = "org.apache.spark" %% "spark-graphx" % Spark % "provided"
    val akkaActor = "com.typesafe.akka" %% "akka-actor" % Akka
    val akkaAgent = "com.typesafe.akka" %% "akka-agent" % Akka
    val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % Akka
    val akkaClusterMetrics = "com.typesafe.akka" %% "akka-cluster-metrics" % Akka
    val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % Akka
    val akkaStream = "com.typesafe.akka" %% "akka-stream" % Akka
    val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core" % AkkaHttp
    val akkaHttp = "com.typesafe.akka" %% "akka-http" % AkkaHttp
    val kafka = "org.apache.kafka" %% "kafka" % Kafka
    val kafkaStream = "org.apache.kafka" % "kafka-streams" % Kafka
  }

  import Library._

  val spark = Seq(sparkCassandraConnector, sparkCore, sparkSql, sparkStreaming, sparkStreamingKafka, sparkGraphx)

  val logging = Seq(logback, scalaLogging, scalaLoggingSlf4j)

  val ta = Seq(technicalAnalysis)

  val time = Seq(jodaTime)

  val config = Seq(scalaConfig)

  val akka = Seq(akkaActor, akkaAgent, akkaCluster, akkaClusterMetrics, akkaSlf4j, akkaStream, akkaHttp, akkaHttpCore)

  // Module dependencies
  val core = time ++ config ++ logging ++ akka

  val client = spark ++ ta

  val app = spark ++ ta ++ akka ++ Seq(kafka, kafkaStream)

  val example = spark ++ ta

}

