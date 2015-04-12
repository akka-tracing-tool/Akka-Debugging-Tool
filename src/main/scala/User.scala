import UnreliableWorker.{MyTypeOfMessage, MessageWithException}
import akka.actor._
import com.sun.javaws.exceptions.InvalidArgumentException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object User {
  def props(unreliableWorker: ActorRef): Props = Props(classOf[User], unreliableWorker)
}

class User(unreliableWorker: ActorRef) extends Actor {
  context.system.scheduler.schedule(1 seconds, 2 seconds, unreliableWorker, "value")
  def receive: Receive = partialTraitDistributedStackTrace()

  def partialTraitDistributedStackTrace(): Receive = {
    case MyTypeOfMessage(1) => println("wszystko ok!")
    case message @ MyTypeOfMessage(0) =>
      val exception = new InvalidArgumentException(Array("0 instead 1"))
      val oldStackTrace = exception.getStackTrace
      val newStackTrace = oldStackTrace ++ message.stackTrace
      exception.setStackTrace(newStackTrace)
      throw exception
  }

  def partialMessageWithException(): Receive = {
    case MessageWithException(1) => println("wszystko ok!")
    case message @ MessageWithException(0) =>
      val exception = new InvalidArgumentException(Array("0 instead 1"))
      val oldStackTrace = exception.getStackTrace
      val newStackTrace = oldStackTrace ++ message.stackTrace
      exception.setStackTrace(newStackTrace)
      throw exception
  }
}

