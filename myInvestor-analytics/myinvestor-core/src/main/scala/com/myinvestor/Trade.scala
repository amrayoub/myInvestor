package com.myinvestor

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.myinvestor.TradeSchema._
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import spray.json.{JsString, _}

/**
  * Trading object.
  */
object Trade {

  /**
    * Generate a type 4 UUID.
    *
    * @return UUID version 4 string.
    */
  def UUIDVersion4: UUID = java.util.UUID.randomUUID

  /**
    * Generate a UUID from string
    *
    * @param uuid UUID string
    * @return An UUID
    */
  def UUIDFromString(uuid: String): UUID = java.util.UUID.fromString(uuid)

  trait JsonApiProtocol extends SprayJsonSupport with DefaultJsonProtocol {

    implicit object DateTimeFormat extends RootJsonFormat[DateTime] {
      val formatter: DateTimeFormatter = ISODateTimeFormat.basicDateTimeNoMillis

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


  trait TradeAggregate extends TradeModel with Serializable {
    def symbol: String
  }

  trait TechnicalAnalysis extends TradeAggregate

  // Moving average 10 days
  case class MovingAverage10(symbol: String) extends TechnicalAnalysis

  // Moving average 20 days
  case class MovingAverage20(symbol: String) extends TechnicalAnalysis


}
