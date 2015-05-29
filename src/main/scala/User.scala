import UnreliableWorker.MyTypeOfMessage
import akka.actor._
import akka_debugging.DistributedStackTrace

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object User {
  def props(unreliableWorker: ActorRef): Props = Props(classOf[User], unreliableWorker)
}

class User(unreliableWorker: ActorRef) extends Actor with DistributedStackTrace {
  val cancellable = context.system.scheduler.schedule(1 seconds, 2 seconds, unreliableWorker, "value")

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    cancellable.cancel()
  }

  def receive: Receive = {
    case MyTypeOfMessage(1) => println("wszystko ok!")
    case MyTypeOfMessage(0) =>
      throw new IllegalArgumentException("0 instead 1")
  }
}

