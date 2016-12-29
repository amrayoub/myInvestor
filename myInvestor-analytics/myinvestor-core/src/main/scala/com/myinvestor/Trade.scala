package com.myinvestor

/**
  * Trading object.
  */
object Trade {

  // Base marker trait.
  @SerialVersionUID(1L)
  sealed trait TradeModel extends Serializable

  // TODO Stock aggregation
  case class StockHistory(symbol: String) extends TradeModel

  object StockHistory {
    // TODO use a JSON object
    def apply(array: Array[String]): StockHistory = {
      StockHistory(
        symbol = array(0)
      )
    }
  }

  trait TradeAggregate extends TradeModel with Serializable {
    def symbol: String
  }

  // TODO Technical analysis
  trait TechnicalAnalysis extends TradeAggregate

  // Moving average 10 days
  case class MovingAverage10(symbol: String) extends TechnicalAnalysis

  // Moving average 20 days
  case class MovingAverage20(symbol: String) extends TechnicalAnalysis

}
