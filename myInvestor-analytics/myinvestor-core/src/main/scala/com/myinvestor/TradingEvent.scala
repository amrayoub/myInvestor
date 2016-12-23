package com.myinvestor

import org.joda.time.DateTime

/**
  * Trading events.
  */
object TradingEvent {

  import Trading._

  // Base marker trait.
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


  sealed trait Task extends Serializable
  case object QueryTask extends Task
  case object GracefulShutdown extends LifeCycleEvent

}
