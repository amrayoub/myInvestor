import sbt._

/**
  * Module dependencies.
  */
object Dependencies {

  import Versions._

  implicit class Exclude(module: ModuleID) {
    def log4jExclude: ModuleID =
      module excludeAll (ExclusionRule("log4j"))
  }

  object Library {

    val technicalAnalysis: ModuleID = "eu.verdelhan" % "ta4j" % TAVersion
    val logback: ModuleID = "ch.qos.logback" % "logback-classic" % Logback
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % ScalaLogging
    val scalaLoggingSlf4j: ModuleID = "com.typesafe.scala-logging" %% "scala-logging-slf4j" % ScalaLoggingSlf4j % "provided"
    val scalaConfig: ModuleID = "com.typesafe" % "config" % ScalaConfig
    val jodaTime: ModuleID = "joda-time" % "joda-time" % JodaTime
    val sparkCassandraConnector: ModuleID = "com.datastax.spark" %% "spark-cassandra-connector" % SparkCassandra % "provided"
    val sparkCore: ModuleID = "org.apache.spark" %% "spark-core" % Spark % "provided"
    val sparkSql: ModuleID = "org.apache.spark" %% "spark-sql" % Spark % "provided"
    val sparkStreaming: ModuleID = "org.apache.spark" %% "spark-streaming" % Spark % "provided"
    val sparkStreamingKafka: ModuleID = "org.apache.spark" %% "spark-streaming-kafka-0-10" % Spark % "provided"
    val sparkGraphx: ModuleID = "org.apache.spark" %% "spark-graphx" % Spark % "provided"
    val akkaActor: ModuleID = "com.typesafe.akka" %% "akka-actor" % Akka
    val akkaAgent: ModuleID = "com.typesafe.akka" %% "akka-agent" % Akka
    val akkaCluster: ModuleID = "com.typesafe.akka" %% "akka-cluster" % Akka
    val akkaClusterMetrics: ModuleID = "com.typesafe.akka" %% "akka-cluster-metrics" % Akka
    val akkaSlf4j: ModuleID = "com.typesafe.akka" %% "akka-slf4j" % Akka
    val akkaStream: ModuleID = "com.typesafe.akka" %% "akka-stream" % Akka
    val akkaHttpCore: ModuleID = "com.typesafe.akka" %% "akka-http-core" % AkkaHttp
    val akkaHttp: ModuleID = "com.typesafe.akka" %% "akka-http" % AkkaHttp
    val kafka: ModuleID = "org.apache.kafka" %% "kafka" % Kafka
    val kafkaStream: ModuleID = "org.apache.kafka" % "kafka-streams" % Kafka
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

