package com.myinvestor

import java.util.UUID

import org.joda.time.DateTime

/**
  * Database schema
  */
object TradeSchema {

  // Keyspace
  val Keyspace = "myinvestor"

  // Tables
  val RequestTable = "request"
  val ExchangeTable = "exchange"
  val StockTable = "stock"
  val StockHistoryTable = "stock_history"
  val StockDetailsTable = "stock_details"

  // Columns
  val RequestIdColumn = "request_id"
  val SuccessColumn = "success"
  val ErrorMsgColumn = "error_msg"
  val ExchangeNameColumn = "exchange_name"


  // Classes

  @SerialVersionUID(1L)
  trait TradeModel extends Serializable

  case class Request(requestId: UUID, success: Boolean, errorMsg: String)

  case class Exchange(exchangeName: String) extends TradeModel

  case class Stock(symbol: String, name: String, exchangeName: String) extends TradeModel

  case class StockHistory(symbol: String, exchangeName: String, date: DateTime,
                          close: Double, high: Double,
                          low: Double, open: Double,
                          volume: Double) extends TradeModel

  case class StockInfo(symbol: String, exchangeName: String, weeks52: String,
                       beta: String, change: String, changePercentage: String,
                       current: String, dividendYield: String, eps: String,
                       instOwn: String, marketCapital: String, open: String,
                       pe: String, range: String, shares: String,
                       time: String, volume: String) extends TradeModel
}
