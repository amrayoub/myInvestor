package com.myinvestor.model

import java.io.Serializable
import java.util.Date

object CassandraModel {

  @SerialVersionUID(1L)
  sealed trait Model extends Serializable


  /**
    * Exchange model.
    *
    * @param exchangeId
    * @param exchangeName
    */
  case class Exchange(exchangeId: Int, exchangeName: String) extends Model

  /**
    * Stock model.
    *
    * @param stockSymbol
    * @param exchangeId
    * @param stockName
    */
  case class Stock(stockSymbol: String, exchangeId: Int, stockName: String) extends Model

  /**
    * Stock history model.
    *
    * @param stockSymbol
    * @param historyDate
    * @param historyClose
    * @param historyHigh
    * @param historyLow
    * @param historyOpen
    * @param historyVolume
    */
  case class StockHistory(stockSymbol: String, historyDate: Date,
                          historyClose: Double, historyHigh: Double,
                          historyLow: Double, historyOpen: Double,
                          historyVolume: Int) extends Model

}