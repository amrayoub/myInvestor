package com.myinvestor

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.myinvestor.TradeEvent.GetStockHistory
//import com.datastax.spark.connector.streaming._   // Enable Cassandra functions on the streaming context
import org.apache.spark.streaming.StreamingContext
/**
  * Aggregation and summary for stock market and stock.
  */
class StockAggregatorActor(ssc: StreamingContext, settings: MyInvestorSettings) extends AggregationActor with ActorLogging {

  import settings.{CassandraKeyspace => keyspace}

  def receive: Actor.Receive = {
    case GetStockHistory(symbol) => history(symbol, sender)
  }

  def history(symbol: String, requester: ActorRef): Unit = {
    // TODO
    println("retrieving history")
    //ssc.cassandraTable("testing").tableName

  }
}

