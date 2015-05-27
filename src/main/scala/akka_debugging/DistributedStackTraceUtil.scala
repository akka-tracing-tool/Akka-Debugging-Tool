package akka_debugging

import akka.actor.Actor

trait DistributedStackTraceMessage {
  val stackTrace = Thread.currentThread().getStackTrace
}

trait DistributedStackTrace {
  self: Actor =>
  override def aroundReceive(receive: Actor.Receive, msg: Any): Unit = {
    try {
      receive.applyOrElse(msg, unhandled)
    } catch {
      //TODO: make aspectj exception handler
      case exception: Exception => println("INTERCEPTED exception " + exception)
        val oldStackTrace = exception.getStackTrace
        val newStackTrace = oldStackTrace ++ msg.asInstanceOf[DistributedStackTraceMessage].stackTrace
        exception.setStackTrace(newStackTrace)
        throw exception
    }
  }
}
