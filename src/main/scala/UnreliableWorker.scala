import UnreliableWorker.MyTypeOfMessage
import akka.actor._
import akka_debugging.DistributedStackTraceMessage

import scala.util.Random

object UnreliableWorker {
  case class MyTypeOfMessage(message: Any) extends DistributedStackTraceMessage
}

class UnreliableWorker extends Actor {
  def receive: Receive = {
    case "value" => {
      sender() ! (if (Random.nextInt() % 3 == 0) MyTypeOfMessage(0) else MyTypeOfMessage(1))
    }
  }
}
