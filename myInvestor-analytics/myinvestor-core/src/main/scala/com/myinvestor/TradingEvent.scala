package com.myinvestor

/**
  * Trading events.
  */
object TradingEvent {

  @SerialVersionUID(1L)
  sealed trait TradingEvent extends Serializable

  sealed trait LifeCycleEvent extends TradingEvent

  case object OutputStreamInitialized extends LifeCycleEvent

  case object NodeInitialized extends LifeCycleEvent

  case object Start extends LifeCycleEvent

  case object DataFeedStarted extends LifeCycleEvent

  case object Shutdown extends LifeCycleEvent

  case object TaskCompleted extends LifeCycleEvent


  // TODO
  sealed trait TradingRequest extends TradingEvent

  // Stock summary and aggregation
  trait StockRequest extends TradingRequest

  case class GetStockHistory(symbol: String) extends StockRequest


  sealed trait Task extends Serializable

  case object QueryTask extends Task

  case object GracefulShutdown extends LifeCycleEvent

}
