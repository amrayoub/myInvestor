import sbt._
import sbt.Keys._

object MyInvestorBuild extends Build {
  import Settings._

  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = parentSettings,
    aggregate = Seq(core, app, client, example)
  )

  lazy val core = Project(
    id = "core",
    base = file("./myinvestor-core"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.core)
  )

  lazy val app = Project(
    id = "app",
    base = file("./myinvestor-app"),
    dependencies = Seq(core),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.app)
  ) configs IntegrationTest

  lazy val client = Project(
    id = "client",
    base = file("./myinvestor-client"),
    dependencies = Seq(core),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.client)
  )

  lazy val example = Project(
    id = "example",
    base = file("./myinvestor-example"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.example)
  )

}

/** To use the connector, the only dependency required is:
  * "com.datastax.spark"  %% "spark-cassandra-connector" and possibly slf4j.
  * The others are here for other non-spark core and streaming code.
  */
object Dependencies {
  import Versions._

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


 object Compile {

    val akkaStream        = "com.typesafe.akka"   %% "akka-stream"                        % AkkaStreams
    val akkaActor         = "com.typesafe.akka"   %% "akka-actor"                         % Akka
    val akkaCluster       = "com.typesafe.akka"   %% "akka-cluster"                       % Akka
    val akkaClusterMetrics = "com.typesafe.akka"   %% "akka-cluster-metrics"              % Akka
    val akkaRemote        = "com.typesafe.akka"   %% "akka-remote"                        % Akka
    val akkaSlf4j         = "com.typesafe.akka"   %% "akka-slf4j"                         % Akka
    val algebird          = "com.twitter"         %% "algebird-core"                      % Albebird
    val bijection         = "com.twitter"         %% "bijection-core"                     % Bijection
    val driver            = "com.datastax.cassandra" % "cassandra-driver-core"            % CassandraDriver driverExclusions
    val jodaTime          = "joda-time"           % "joda-time"                           % JodaTime   % "compile;runtime" // ApacheV2
    val jodaConvert       = "org.joda"            % "joda-convert"                        % JodaConvert % "compile;runtime" // ApacheV2
    val json4sCore        = "org.json4s"          %% "json4s-core"                        % Json4s          // ApacheV2
    val json4sJackson     = "org.json4s"          %% "json4s-jackson"                     % Json4s          // ApacheV2
    val json4sNative      = "org.json4s"          %% "json4s-native"                      % Json4s          // ApacheV2
    val kafka             = "org.apache.kafka"    %% "kafka"                              % Kafka kafkaExclusions // ApacheV2
    val kafkaStreaming    = "org.apache.spark"    %% "spark-streaming-kafka-0-10"         % Spark sparkExclusions // ApacheV2
    val logback           = "ch.qos.logback"      % "logback-classic"                     % Logback
    val slf4jApi          = "org.slf4j"           % "slf4j-api"                           % Slf4j           // MIT
    val sparkML           = "org.apache.spark"    %% "spark-mllib"                        % Spark sparkExclusions // ApacheV2
    val sparkCatalyst     = "org.apache.spark"    %% "spark-catalyst"                     % Spark sparkExclusions
    val sparkCassandra    = "com.datastax.spark"  %% "spark-cassandra-connector"          % SparkCassandra // ApacheV2
    val sparkCassandraEmb = "com.datastax.spark"  %% "spark-cassandra-connector-embedded" % SparkCassandra embeddedExclusions // ApacheV2
    val sigar             = "org.fusesource"      % "sigar"                               % Sigar
    val scalaLogging      = "com.typesafe.scala-logging" %% "scala-logging"               % ScalaLogging
    val scalaLoggingSlf4j = "com.typesafe.scala-logging" %% "scala-logging-slf4j"         % ScalaLoggingSlf4j
    val technicalAnalysis = "eu.verdelhan"        % "ta4j"                                % TAVersion
  }

  object Test {
    val akkaTestKit     = "com.typesafe.akka"     %% "akka-testkit"                       % Akka      % "test,it" // ApacheV2
    val scalatest       = "org.scalatest"         %% "scalatest"                          % ScalaTest % "test,it"
  }

  import Compile._

  val akka = Seq(akkaStream, akkaActor, akkaCluster, akkaRemote, akkaSlf4j, akkaClusterMetrics)

  val connector = Seq(driver, sparkCassandra, sparkCatalyst, sparkCassandraEmb)

  val json = Seq(json4sCore, json4sJackson, json4sNative)

  val logging = Seq(logback, slf4jApi, scalaLoggingSlf4j, scalaLogging)

  val time = Seq(jodaConvert, jodaTime)

  val test = Seq(Test.akkaTestKit, Test.scalatest)

  val ta = Seq(technicalAnalysis)

  /** Module deps */
  val client = akka ++ logging ++ Seq(sparkCassandraEmb, sigar)

  val core = akka ++ logging ++ time

  val app = connector ++ json ++ test ++ ta ++
    Seq(algebird, bijection, kafka, kafkaStreaming, sparkML, sigar)

  val example = connector ++ time ++ json ++
    Seq(kafka, kafkaStreaming, sparkML, "org.slf4j" % "slf4j-log4j12" % "1.7.21")
}

