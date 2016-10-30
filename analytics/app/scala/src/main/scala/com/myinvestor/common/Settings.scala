package com.myinvestor

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Application settings.
  */
class Settings(config: Config) {

  var sparkMaster: String
  val cassandraHost: String

  //config.checkValid(ConfigFactory.defaultReference(), "myInvestor")

  def this() {
    this(ConfigFactory.load("myInvestor"))
    sparkMaster
  }

  /**
    * Use for debugging purpose only.
    *
    * @param path Path to the parameter
    */
  def printSetting(path: String) {
    println("The setting '" + path + "' is: " + config.getString(path))
  }
}
