import UnreliableWorker.MyTypeOfMessage
import akka.actor._
import com.sun.javaws.exceptions.InvalidArgumentException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object User {
  def props(unreliableWorker: ActorRef): Props = Props(classOf[User], unreliableWorker)
}

class User(unreliableWorker: ActorRef) extends Actor with DistributedStackTrace {
  context.system.scheduler.schedule(1 seconds, 2 seconds, unreliableWorker, "value")

  def receive: Receive = {
    case MyTypeOfMessage(1) => println("wszystko ok!")
    case MyTypeOfMessage(0) =>
      throw new InvalidArgumentException(Array("0 instead 1"))
  }
}

