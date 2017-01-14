package com.myinvestor

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.myinvestor.Trade.{MovingAverage10, MovingAverage20}
import org.apache.spark.SparkContext

/**
  * Perform technical analysis.
  */
class TechnicalAnalysisActor(sc: SparkContext, settings: AppSettings) extends AggregationActor with ActorLogging {
  def receive: Actor.Receive = {
    case e: MovingAverage10 => movingAverage10(e.symbol, sender)
    case e: MovingAverage20 => movingAverage20(e.symbol, sender)
  }

  def movingAverage10(symbol: String, requester: ActorRef): Unit = {
    // TODO
  }

  def movingAverage20(symbol: String, requester: ActorRef): Unit = {
    // TODO
  }
}
