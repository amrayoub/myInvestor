import sbt._

/**
  *
  * Module dependencies.
  *
  */
object Dependencies {

  import Versions._
  
  /*
  implicit class Exclude(module: ModuleID) {
    def log4jExclude: ModuleID =
      module excludeAll(ExclusionRule("log4j"))

    def embeddedExclusions: ModuleID =
      module.log4jExclude.excludeAll(ExclusionRule("org.apache.spark"))
        .excludeAll(ExclusionRule("com.typesafe"))
        .excludeAll(ExclusionRule("org.apache.cassandra"))
        .excludeAll(ExclusionRule("com.datastax.cassandra"))

    def driverExclusions: ModuleID =
      module.log4jExclude.exclude("com.google.guava", "guava")
        .excludeAll(ExclusionRule("org.slf4j"))

    def sparkExclusions: ModuleID =
      module.log4jExclude.exclude("com.google.guava", "guava")
        .exclude("org.apache.spark", "spark-core")
        .exclude("org.slf4j", "slf4j-log4j12")

    def kafkaExclusions: ModuleID =
      module.log4jExclude.excludeAll(ExclusionRule("org.slf4j"))
        .exclude("com.sun.jmx", "jmxri")
        .exclude("com.sun.jdmk", "jmxtools")
        .exclude("net.sf.jopt-simple", "jopt-simple")
  }
  */

  object Library {

    val technicalAnalysis = "eu.verdelhan" % "ta4j"% TAVersion    
    val logback = "ch.qos.logback" % "logback-classic" % Logback
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % ScalaLogging
    val scalaLoggingSlf4j = "com.typesafe.scala-logging" %% "scala-logging-slf4j" %  ScalaLoggingSlf4j
    val scalaConfig = "com.typesafe" % "config" % ScalaConfig
    val jodaTime = "joda-time" % "joda-time" % JodaTime
    val sparkCassandraConnector = "com.datastax.spark" %% "spark-cassandra-connector" % SparkCassandra
    val sparkCore = "org.apache.spark" %% "spark-core" % Spark
    val sparkSql = "org.apache.spark" %% "spark-sql" % Spark
    val sparkStreaming = "org.apache.spark" %% "spark-streaming" % Spark
    val sparkGraphx = "org.apache.spark" %% "spark-graphx" % Spark

  }

  import Library._

  //val akka = Seq(akkaStream, akkaActor, akkaCluster, akkaRemote, akkaSlf4j, akkaClusterMetrics)

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

