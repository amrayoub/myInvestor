package com.myinvestor

import org.joda.time.DateTime

/**
  * Trading object.
  */
object Trade {

  val NameField: String = "name"

  @SerialVersionUID(1L)
  sealed trait TradeModel extends Serializable

  case class Exchange(exchangeId: Int, exchangeName: String) extends TradeModel

  case class Stock(symbol: String, exchangeId: Int, name: String) extends TradeModel

  object Stock {
    def apply(jsonObject: String): Stock = {
      Stock(
        symbol = "",
        exchangeId = 0,
        name = ""
      )
    }
  }

  case class StockHistory(symbol: String, date: DateTime,
                          close: Double, high: Double,
                          low: Double, open: Double,
                          volume: Double) extends TradeModel

  object StockHistory {
    def apply(jsonObject: String): StockHistory = {
      StockHistory(
        symbol = "",
        date = DateTime.now,
        close = 0.0,
        high = 0.0,
        low = 0.0,
        open = 0.0,
        volume = 0.0
      )
    }
  }

  case class StockInfo(symbol: String) extends TradeModel


  trait TradeAggregate extends TradeModel with Serializable {
    def symbol: String
  }

  trait TechnicalAnalysis extends TradeAggregate

  // Moving average 10 days
  case class MovingAverage10(symbol: String) extends TechnicalAnalysis

  // Moving average 20 days
  case class MovingAverage20(symbol: String) extends TechnicalAnalysis

}
