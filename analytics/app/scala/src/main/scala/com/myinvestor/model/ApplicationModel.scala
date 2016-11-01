package com.myinvestor.model

import java.io.Serializable

import eu.verdelhan.ta4j.Tick

/**
  * Application specific object modeling.
  */
object ObjectModel {

  case class StockTick extends Tick implements Serializable
}
