package com.myinvestor

import akka.actor.{ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}

/**
  * Main application to start Kafka, ZooKeeper, Akka.
  *
  * Can be run with sbt: sbt app/run
  */
object MyInvestorApp extends App {
}


object MyInvestor extends ExtensionId[MyInvestor] with ExtensionIdProvider {

  override def lookup: ExtensionId[_ <: Extension] = MyInvestor

  override def createExtension(system: ExtendedActorSystem) = new MyInvestor(system)

}


class MyInvestor(system: ExtendedActorSystem) extends Extension {

}