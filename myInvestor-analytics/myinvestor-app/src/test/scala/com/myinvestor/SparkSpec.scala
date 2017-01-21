package com.myinvestor

import org.apache.spark.SparkContext
import org.apache.spark.streaming.StreamingContext
import org.scalatest.{BeforeAndAfterAll, Failed, Outcome, Suite}

/**
  */
trait SparkSpec extends BeforeAndAfterAll {
  this: Suite =>

  override def beforeAll(): Unit = {
    super.beforeAll()


  }

  override def afterAll(): Unit = {

  }

}
