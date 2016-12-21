package com.myinvestor

/**
  * Trading events.
  */
object TradingEvent {

  import Trading._

  // Base marker trait.
  @SerialVersionUID(1L)
  sealed trait TradingEvent extends Serializable

  sealed trait LifeCycleEvent extends TradingEvent

  case object GracefulShutdown extends LifeCycleEvent

}
