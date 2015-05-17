package akka_debugging.collector

import java.io.FileWriter
import java.util.UUID

import akka.actor.{Actor, ActorRef}

object Collector {
  case class CollectorMessage(actorRef: ActorRef, id: UUID, message: Any, stackTrace: Array[StackTraceElement])
  case class CollectorExceptionMessage(actorRef: ActorRef, id: UUID, exception: Exception)
}

class Collector extends Actor {

  import Collector._

  val fileWriter = new FileWriter("collector.txt", true)
  def receive: Receive = {
    case CollectorMessage(actorRef, uuid, msg, stackTrace) =>
      val niceStackTrace = stackTrace.filter(el => !el.toString.startsWith("akka") && !el.toString.startsWith("scala"))
      fileWriter.write(String.format("===========\nMESSAGE\n===========\n%s\n%s\n%s\n%s\n",
        actorRef.toString, uuid.toString, msg.toString, niceStackTrace.mkString("\n")))
      fileWriter.flush()
    case CollectorExceptionMessage(actorRef: ActorRef, uuid: UUID, exception: Exception) =>
      val niceStackTrace = exception.getStackTrace.filter(
        el => !el.toString.startsWith("akka") && !el.toString.startsWith("scala"))
      fileWriter.write(String.format("===========\nEXCEPTION\n===========\n%s\n%s\n%s\n%s\n",
        actorRef.toString, uuid.toString, exception.getClass.getCanonicalName, niceStackTrace.mkString("\n")))
      fileWriter.flush()
  }

}
