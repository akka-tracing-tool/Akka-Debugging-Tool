import java.util.UUID

import Collector.{CollectorExceptionMessage, CollectorMessage}
import akka.actor.Actor
import akka.util.Timeout
import scala.concurrent.duration._

trait DistributedStackTraceMessage {
  val stackTrace = Thread.currentThread().getStackTrace
}

trait DistributedStackTrace { self: Actor =>
  override def aroundReceive(receive: Actor.Receive, msg: Any): Unit = {
    val id: UUID = UUID.randomUUID()
    try {
      implicit val timeout = Timeout(3.seconds)
      context.actorSelection("/user/collector") ! CollectorMessage(this.self, id, msg, Thread.currentThread().getStackTrace)
      receive.applyOrElse(msg, unhandled)
    } catch {
      case exception: Exception => println("INTERCEPTED exception " + exception)
        val oldStackTrace = exception.getStackTrace
        val newStackTrace = oldStackTrace ++ msg.asInstanceOf[DistributedStackTraceMessage].stackTrace
        exception.setStackTrace(newStackTrace)
        context.actorSelection("/user/collector") ! CollectorExceptionMessage(this.self, id, exception)
      throw exception
    }
  }
}