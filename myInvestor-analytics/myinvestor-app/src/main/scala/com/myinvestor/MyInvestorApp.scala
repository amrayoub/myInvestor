package com.myinvestor

import akka.actor.{ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}

/**
  * Main application to start Kafka, ZooKeeper, Akka.
  *
  * Can be run with sbt: sbt app/run
  */
object MyInvestorApp extends App {
  val settings = new MyInvestorSettings
  import settings._

  // Create the Actor system
  val system = ActorSystem(AppName)
  val myInvestor = MyInvestor(system)

}


object MyInvestor extends ExtensionId[MyInvestor] with ExtensionIdProvider {

  override def lookup: ExtensionId[_ <: Extension] = MyInvestor

  override def createExtension(system: ExtendedActorSystem) = new MyInvestor(system)

}


class MyInvestor(system: ExtendedActorSystem) extends Extension {

}