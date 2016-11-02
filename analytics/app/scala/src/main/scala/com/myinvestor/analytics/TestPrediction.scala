package com.myinvestor.analytics

import java.util

import com.myinvestor.model.CassandraModel.{Stock, StockHistory}
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator
import eu.verdelhan.ta4j.{Tick, TimeSeries}
import org.apache.spark.rdd.RDD

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
    val series = new TimeSeries(stock.stockSymbol, ticks)

    // Building the trading strategy
    val firstClosePrice = series.getTick(0).getClosePrice();
    println(firstClosePrice.toDouble)

    val closePrice = new ClosePriceIndicator(series)
    println(firstClosePrice.isEqual(closePrice.getValue(0)))

    val shortSma = new SMAIndicator(closePrice, 5)
    println("5-ticks-SMA value at the 42nd index: " + shortSma.getValue(42).toDouble())

    val longSma = new SMAIndicator(closePrice, 30)

  }
}
