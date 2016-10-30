package com.myinvestor.analytics

import com.myinvestor.model.CassandraModel.Stock
import org.apache.spark.SparkContext

/**
  * Simple prediction used for testing.
  */
class SimplePrediction extends Serializable {

  def perform(sc: SparkContext, stock: Stock) {
    println(stock.stockName)
  }
}
