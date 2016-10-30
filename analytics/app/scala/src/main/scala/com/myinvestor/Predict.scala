package com.myinvestor

import com.datastax.spark.connector._
import com.myinvestor.analytics.SimplePrediction
import com.myinvestor.common.Settings
import com.myinvestor.model.CassandraModel.{Stock, StockHistory}
import com.myinvestor.model.CassandraSchema
import com.typesafe.scalalogging.Logger
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Perform prediction.
  */
object Predict {

  def main(args: Array[String]) {
    val logger = Logger("Predict")
    val settings = new Settings()

    val sparkConf = new SparkConf(true)
      .set("spark.cassandra.connection.host", settings.cassandraHost)
      .set("spark.cassandra.auth.username", settings.cassandraUser)
      .set("spark.cassandra.auth.password", settings.cassandraUserPassword)
    val sc = new SparkContext(settings.sparkMaster, settings.sparkAppName, sparkConf)

    // Get the stock as list instead of RDD
    val stocks = sc.cassandraTable[Stock](CassandraSchema.KEYSPACE, CassandraSchema.STOCK_TABLE).where("exchange_id = ?", settings.exchangeId).collect()
    stocks.foreach { stock =>
      // Get the stock history for this stock
      val stockHistory = sc.cassandraTable[StockHistory](CassandraSchema.KEYSPACE, CassandraSchema.STOCK_HISTORY_TABLE).select(
        CassandraSchema.STOCK_SYMBOL_COLUMN, CassandraSchema.HISTORY_DATE, CassandraSchema.HISTORY_CLOSE, CassandraSchema.HISTORY_HIGH,
        CassandraSchema.HISTORY_LOW, CassandraSchema.HISTORY_OPEN, CassandraSchema.HISTORY_VOLUME
      ).where(CassandraSchema.STOCK_SYMBOL_COLUMN + " = ?", stock.stockSymbol)

      stockHistory.foreach { history =>
        print(history.stockSymbol)
        println(history.historyDate)
      }

    }

    /*
    val printStockSymbol = (stock: StockHistory) => {
      println(stock.stockSymbol)
      println(stock.historyDate)
    }
    */

    //stockHistory.foreach(printStockSymbol)

  }
}
