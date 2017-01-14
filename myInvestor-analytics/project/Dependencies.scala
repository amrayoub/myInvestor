import sbt._

/**
  * Module dependencies.
  */
object Dependencies {

  import Versions._

  implicit class Exclude(module: ModuleID) {
    def log4jExclude: ModuleID =
      module excludeAll (ExclusionRule("log4j"))

    def sparkExclusions: ModuleID =
      module.log4jExclude
        .exclude("org.apache.spark", "spark-core")
        .exclude("org.slf4j", "slf4j-log4j12")

    def cassandraExclusions: ModuleID =
      module.log4jExclude.exclude("com.google.guava", "guava")
        .excludeAll(ExclusionRule("org.slf4j"))

    def kafkaExclusions: ModuleID =
      module.log4jExclude.excludeAll(ExclusionRule("org.slf4j"))
        .exclude("com.sun.jmx", "jmxri")
        .exclude("com.sun.jdmk", "jmxtools")
        .exclude("net.sf.jopt-simple", "jopt-simple")

  }

  object Library {
    val technicalAnalysis: ModuleID = "eu.verdelhan" % "ta4j" % TAVersion
    val logback: ModuleID = "ch.qos.logback" % "logback-classic" % Logback
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % ScalaLogging
    val scalaLoggingSlf4j: ModuleID = "com.typesafe.scala-logging" %% "scala-logging-slf4j" % ScalaLoggingSlf4j % "provided"
    val scalaConfig: ModuleID = "com.typesafe" % "config" % ScalaConfig
    val jodaTime: ModuleID = "joda-time" % "joda-time" % JodaTime % "compile;runtime"
    val sparkCassandraConnector: ModuleID = "com.datastax.spark" %% "spark-cassandra-connector" % SparkCassandra
    val sparkCore: ModuleID = "org.apache.spark" %% "spark-core" % Spark
    val sparkSql: ModuleID = "org.apache.spark" %% "spark-sql" % Spark sparkExclusions
    val sparkStreaming: ModuleID = "org.apache.spark" %% "spark-streaming" % Spark sparkExclusions
    val sparkStreamingKafka: ModuleID = "org.apache.spark" %% "spark-streaming-kafka-0-10" % Spark sparkExclusions
    val sparkGraphx: ModuleID = "org.apache.spark" %% "spark-graphx" % Spark sparkExclusions
    val akkaActor: ModuleID = "com.typesafe.akka" %% "akka-actor" % Akka
    val akkaAgent: ModuleID = "com.typesafe.akka" %% "akka-agent" % Akka
    val akkaCluster: ModuleID = "com.typesafe.akka" %% "akka-cluster" % Akka
    val akkaClusterMetrics: ModuleID = "com.typesafe.akka" %% "akka-cluster-metrics" % Akka
    val akkaSlf4j: ModuleID = "com.typesafe.akka" %% "akka-slf4j" % Akka
    val akkaStream: ModuleID = "com.typesafe.akka" %% "akka-stream" % Akka
    val akkaHttpCore: ModuleID = "com.typesafe.akka" %% "akka-http-core" % AkkaHttp
    val akkaHttpJson:ModuleID = "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttp
    val akkaHttp: ModuleID = "com.typesafe.akka" %% "akka-http" % AkkaHttp
    val kafka: ModuleID = "org.apache.kafka" %% "kafka" % Kafka kafkaExclusions
    val kafkaStream: ModuleID = "org.apache.kafka" % "kafka-streams" % Kafka kafkaExclusions
    val cassandraDriverCore: ModuleID = "com.datastax.cassandra" % "cassandra-driver-core" % Cassandra cassandraExclusions
    val cassandraDriverMapping: ModuleID = "com.datastax.cassandra" % "cassandra-driver-mapping" % Cassandra cassandraExclusions
  }

  import Library._

  val cassandra = Seq(cassandraDriverCore, cassandraDriverMapping)

  val spark = Seq(sparkCassandraConnector, sparkCore, sparkSql, sparkStreaming, sparkStreamingKafka, sparkGraphx)

  val logging = Seq(logback, scalaLogging, scalaLoggingSlf4j)

  val ta = Seq(technicalAnalysis)

  val time = Seq(jodaTime)

  val config = Seq(scalaConfig)

  val akka = Seq(akkaActor, akkaAgent, akkaCluster, akkaClusterMetrics, akkaSlf4j, akkaStream, akkaHttp, akkaHttpCore, akkaHttpJson)

  // Module dependencies
  val core = time ++ config ++ logging ++ akka

  val client = spark ++ ta ++ akka ++ cassandra

  val app = spark ++ ta ++ akka ++ Seq(kafka, kafkaStream)

  val example = spark ++ ta

}

