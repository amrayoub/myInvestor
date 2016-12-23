import java.util.concurrent.TimeoutException

import scala.concurrent.duration._
import akka.actor.SupervisorStrategy._
import akka.actor._
import akka.util.Timeout
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}

/**
  * Base actor for data computation.
  */
private[myinvestor] trait AggregationActor extends Actor {

  implicit val timeout = Timeout(5.seconds)

  implicit val ctx = context.dispatcher

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.minute) {
      case _: ActorInitializationException => Stop
      case _: IllegalArgumentException => Stop
      case _: IllegalStateException => Restart
      case _: TimeoutException => Escalate
      case _: Exception => Escalate
    }

  // Creates a timestamp for the current date time in UTC.
  def timestamp: DateTime = new DateTime(DateTimeZone.UTC)

  // Creates a lazy date stream, where elements are only evaluated when they are needed.
  def streamDays(from: DateTime): Stream[DateTime] = from #:: streamDays(from.plusDays(1))

  def isValid(current: DateTime, start: DateTime): Boolean = current.getYear == start.getYear && current.isBeforeNow

  // Creates timestamp for a given year and day of year.
  def dayOfYearForYear(doy: Int, year: Int): DateTime = timestamp.withYear(year).withDayOfYear(doy)

  // Creates timestamp for a given year and day of year.
  def monthOfYearForYear(month: Int, year: Int): DateTime = timestamp.withYear(year).withMonthOfYear(month)

  def toDateFormat(dt: DateTime): String = DateTimeFormat.forPattern("EEEE, MMMM dd, yyyy").print(dt)

}

