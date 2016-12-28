package com.myinvestor

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.pipe
import com.datastax.spark.connector.streaming._
import org.apache.spark.streaming.StreamingContext

/**
  * Give a stock symbol, perform technical analysis.
  */
class StockActor(ssc: StreamingContext, settings: MyInvestorSettings)
  extends AggregationActor with ActorLogging {

  import settings.{CassandraKeyspace => keyspace, CassandraTableDailyPrecip => dailytable}

  def receive: Actor.Receive = {
    case GetPrecipitation(wsid, year) => cumulative(wsid, year, sender)
    case GetTopKPrecipitation(wsid, year, k) => topK(wsid, year, k, sender)
  }


  /**
    *
    * @param wsid
    * @param year
    * @param k
    * @param requester
    */
  def topK(wsid: String, year: Int, k: Int, requester: ActorRef): Unit = {
    val toTopK = (aggregate: Seq[Double]) => TopKPrecipitation(wsid, year,
      ssc.sparkContext.parallelize(aggregate).top(k).toSeq)

    ssc.cassandraTable[Double](keyspace, dailytable)
      .select("precipitation")
      .where("wsid = ? AND year = ?", wsid, year)
      .collectAsync().map(toTopK) pipeTo requester
  }
}

