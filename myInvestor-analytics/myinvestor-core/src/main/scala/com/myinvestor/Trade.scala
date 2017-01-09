package com.myinvestor

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.json.{JsString, _}

/**
  * Trading object.
  */
object Trade {

  @SerialVersionUID(1L)
  sealed trait TradeModel extends Serializable

  /**
    * Generate a type 4 UUID.
    * @return UUID version 4 string.
    */
  def UUIDVersion4 = java.util.UUID.randomUUID.toString

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


  trait JsonApiProtocol extends SprayJsonSupport with DefaultJsonProtocol {

    implicit object DateTimeFormat extends RootJsonFormat[DateTime] {
      val formatter = ISODateTimeFormat.basicDateTimeNoMillis

      def write(obj: DateTime): JsValue = {
        JsString(formatter.print(obj))
      }

      def read(json: JsValue): DateTime = json match {
        case JsString(s) => try {
          formatter.parseDateTime(s)
        }
        catch {
          case t: Throwable => error(s)
        }
        case _ =>
          error(json.toString())
      }

      def error(v: Any): DateTime = {
        val example = formatter.print(0)
        deserializationError(f"'$v' is not a valid date value. Dates must be in compact ISO-8601 format, e.g. '$example'")
      }
    }

    implicit val exchangeFormat = jsonFormat1(Exchange)
    implicit val stockFormat = jsonFormat3(Stock)
    implicit val stockHistoryFormat = jsonFormat8(StockHistory)
    implicit val stockInfoFormat = jsonFormat17(StockInfo)
  }

  /*
  object Stock {
    def apply(jsonObject: String): Stock = {
      Stock(
        symbol = "",
        exchangeId = 0,
        name = ""
      )
    }
  }




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
  */


  trait TradeAggregate extends TradeModel with Serializable {
    def symbol: String
  }

  trait TechnicalAnalysis extends TradeAggregate

  // Moving average 10 days
  case class MovingAverage10(symbol: String) extends TechnicalAnalysis

  // Moving average 20 days
  case class MovingAverage20(symbol: String) extends TechnicalAnalysis


}
