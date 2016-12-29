package com.myinvestor

/**
  * Trading events.
  */
object TradeEvent {

  @SerialVersionUID(1L)
  sealed trait TradeEvent extends Serializable

  sealed trait LifeCycleEvent extends TradeEvent

  case object OutputStreamInitialized extends LifeCycleEvent

  case object NodeInitialized extends LifeCycleEvent

  case object Start extends LifeCycleEvent

  case object DataFeedStarted extends LifeCycleEvent

  case object Shutdown extends LifeCycleEvent

  case object TaskCompleted extends LifeCycleEvent


  // TODO
  sealed trait TradeRequest extends TradeEvent

  // Stock summary and aggregation
  trait StockRequest extends TradeRequest

  case class GetStockHistory(symbol: String) extends StockRequest

  trait TechnicalAnalysisRequest extends TradeRequest

  sealed trait Task extends Serializable

  case object QueryTask extends Task

  case object GracefulShutdown extends LifeCycleEvent

}
