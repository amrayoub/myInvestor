package com.myinvestor

/**
  * Trading object.
  */
object Trading {

  // Base marker trait.
  @SerialVersionUID(1L)
  sealed trait TradingModel extends Serializable

  // TODO
  case class RawTradingData() extends TradingModel

  object RawTradingData {
    // TODO
    def apply(array: Array[String]): RawTradingData = {
      RawTradingData
    }
  }

}
