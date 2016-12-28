package com.myinvestor

/**
  * Trading object.
  */
object Trading {

  // Base marker trait.
  @SerialVersionUID(1L)
  sealed trait TradeModel extends Serializable

  // TODO
  case class StockHistory(symbol: String) extends TradeModel

  object StockHistory {
    // TODO use a JSON object
    def apply(array: Array[String]): StockHistory = {
      StockHistory(
        symbol = array(0)
      )
    }
  }

}
