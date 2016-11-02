package com.myinvestor.analytics

import java.util

import com.myinvestor.model.ApplicationModel.StockTick
import com.myinvestor.model.CassandraModel.{Stock, StockHistory}
import eu.verdelhan.ta4j.{Strategy, Tick, TimeSeries}
import org.apache.spark.rdd.RDD

import scala.collection.JavaConversions._

/**
  * Simple prediction used for testing.
  */
class TestPrediction {

  def apply(stock: Stock, rdd: RDD[StockHistory]): Unit = {

    // ta4j required ticks
    val ticks = new util.ArrayList[Tick]()

    // Transform to ta4j Tick
    val histories = rdd.collect()
    histories.foreach { history =>
      ticks.add(new Tick(history.historyDate, history.historyOpen, history.historyHigh, history.historyLow, history.historyClose, history.historyVolume))
    }
    val timeService = new TimeSeries(stock.stockSymbol, ticks)

    // Building the trading strategy
    val strategy  = MovingMomentumStrategy.buildStrategy(series);

  }
}
