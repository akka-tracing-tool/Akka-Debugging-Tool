import UnreliableWorker.MyTypeOfMessage
import akka.actor._

import scala.util.Random

object UnreliableWorker {
  case class MessageWithException(message: Any) {
    val stackTrace = Thread.currentThread().getStackTrace
  }
  case class MyTypeOfMessage(message: Any) extends DistributedStackTrace
}

trait DistributedStackTrace {
  val stackTrace = Thread.currentThread().getStackTrace
}

trait SendingStackTrace extends ScalaActorRef { ref: ActorRef =>
  override def !(message: Any)(implicit sender: ActorRef): Unit = ???
}


class UnreliableWorker extends Actor {
  def receive: Receive = {
    case "value" => {
//      sender() ! (if (Random.nextInt() % 3 == 0) MessageWithException(0) else MessageWithException(1))
      sender() ! (if (Random.nextInt() % 3 == 0) MyTypeOfMessage(0) else MyTypeOfMessage(1))
    }
  }
}
