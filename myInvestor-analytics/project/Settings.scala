import scala.language.postfixOps

import sbt._
import sbt.Keys._
import net.virtualvoid.sbt.graph.Plugin.graphSettings
import com.scalapenos.sbt.prompt.SbtPrompt.autoImport._

object Settings extends Build {

  lazy val buildSettings = Seq(
    name := "myInvestor",
    normalizedName := "myInvestor",
    organization := "com.myInvestor",
    organizationHomepage := Some(url("http://www.github.com/mengwangk/myinvestor")),
    scalaVersion := Versions.Scala,
    homepage := Some(url("https://github.com/mengwangk/myInvestor")),
    licenses := Seq(("Apache License, Version 2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))),
    promptTheme := ScalapenosTheme
  )

  override lazy val settings = super.settings ++ buildSettings

  val parentSettings = buildSettings ++ Seq(
    publishArtifact := false,
    publish := {}
  )

  lazy val defaultSettings = testSettings ++ graphSettings ++ Seq(
    autoCompilerPlugins := true,
    // removed "-Xfatal-warnings" as temporary workaround for log4j fatal error.
    scalacOptions ++= Seq("-encoding", "UTF-8", s"-target:jvm-${Versions.JDK}", "-feature", "-language:_", "-deprecation", "-unchecked", "-Xlint"),
    javacOptions in Compile ++= Seq("-encoding", "UTF-8", "-source", Versions.JDK, "-target", Versions.JDK, "-Xlint:deprecation", "-Xlint:unchecked"),
    run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)),
    ivyLoggingLevel in ThisBuild := UpdateLogging.Quiet,
    parallelExecution in ThisBuild := false,
    parallelExecution in Global := false
  )

  val tests = inConfig(Test)(Defaults.testTasks) ++ inConfig(IntegrationTest)(Defaults.itSettings)

  val testOptionSettings = Seq(
    Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
  )

  lazy val testSettings = tests ++ Seq(
    parallelExecution in Test := false,
    parallelExecution in IntegrationTest := false,
    testOptions in Test ++= testOptionSettings,
    testOptions in IntegrationTest ++= testOptionSettings,
    baseDirectory in Test := baseDirectory.value.getParentFile(),
    fork in Test := true,
    fork in IntegrationTest := true,
    (compile in IntegrationTest) <<= (compile in Test, compile in IntegrationTest) map { (_, c) => c },
    managedClasspath in IntegrationTest <<= Classpaths.concat(managedClasspath in IntegrationTest, exportedProducts in Test)
  )

}
