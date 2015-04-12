import UnreliableWorker.{MyTypeOfMessage, MessageWithException}
import akka.actor._
import com.sun.javaws.exceptions.InvalidArgumentException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object User {
  def props(unreliableWorker: ActorRef): Props = Props(classOf[User], unreliableWorker)
}

trait DistributedStackTrace { self: Actor =>
//  def receiveLogging: PartialFunction[Any, Any] = {
//    case message: Any =>
//      println("receiveuje " + message)
//      message
//  }
//  def receiveLogging(fun: Receive): Receive = {
//    case message: Any =>
//      println("receiveuje " + message)
//      fun()
//  }

  override def aroundReceive(receive: Actor.Receive, msg: Any): Unit = {
    try {
      receive.applyOrElse(msg, unhandled)
    } catch {
      case exception: Exception => println("INTERCEPTED exception " + exception)
        val oldStackTrace = exception.getStackTrace
        val newStackTrace = oldStackTrace ++ msg.asInstanceOf[DistributedStackTraceMessage].stackTrace
        exception.setStackTrace(newStackTrace)
        throw exception
    }
  }
}
class User(unreliableWorker: ActorRef) extends Actor with DistributedStackTrace {
  context.system.scheduler.schedule(1 seconds, 2 seconds, unreliableWorker, "value")
//  def receive: Receive = receiveLogging andThen partialTraitDistributedStackTrace()
  def receive: Receive = {
    case MyTypeOfMessage(1) => println("wszystko ok!")
    case message @ MyTypeOfMessage(0) =>
      throw new InvalidArgumentException(Array("0 instead 1"))
  }
  def partialTraitDistributedStackTrace: Receive = {
    case MyTypeOfMessage(1) => println("wszystko ok!")
    case message @ MyTypeOfMessage(0) =>
//      val exception = new InvalidArgumentException(Array("0 instead 1"))
//      val oldStackTrace = exception.getStackTrace
//      val newStackTrace = oldStackTrace ++ message.stackTrace
//      exception.setStackTrace(newStackTrace)
//      throw exception

    throw new InvalidArgumentException(Array("0 instead 1"))
  }

  def partialMessageWithException: Receive = {
    case MessageWithException(1) => println("wszystko ok!")
    case message @ MessageWithException(0) =>
      val exception = new InvalidArgumentException(Array("0 instead 1"))
      val oldStackTrace = exception.getStackTrace
      val newStackTrace = oldStackTrace ++ message.stackTrace
      exception.setStackTrace(newStackTrace)
      throw exception
  }
}

