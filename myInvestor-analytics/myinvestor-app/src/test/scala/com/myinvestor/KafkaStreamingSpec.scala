package com.myinvestor

import org.apache.spark.SparkContext
import org.scalatest.BeforeAndAfter

/**
  * Test suite for Kafka.
  */
class KafkaStreamingSpec extends UnitTestSpec with BeforeAndAfter {

  var sparkContext: SparkContext = _

  before {
    sparkContext = SparkContextUtils.sp
  }

  after {
  }

  "A valid exchange JSON" should "be converted correctly to a correct Exchange object" in {


  }

}

