package com.myInvestor

import com.myinvestor.common.Settings

/**
  * Cassandra basic testing.
  */
object TestApp {

  def main(args: Array[String]) {

    val settings = new Settings()
    println(settings.sparkMaster)
    // settings.printSetting("myInvestor.sparkMaster")

    //val logger = Logger("test")
    //logger.info("testing")

    /*
    val sparkMaster = args(0)
    val cassandraHost = args(1)
    val conf = new SparkConf(true).set("spark.cassandra.connection.host", cassandraHost)
    val sc = new SparkContext(sparkMaster, "CassandraApp", conf)
    val data = sc.cassandraTable("myinvestor", "exchange")

    val timeSeries

    println(data.count)
    println(data.first)
    */
  }

}

