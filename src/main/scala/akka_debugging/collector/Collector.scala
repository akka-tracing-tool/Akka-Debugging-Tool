package akka_debugging.collector

import java.io.FileWriter
import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka_debugging.collector.Collector.{CollectorExceptionMessage, CollectorMessage}
import com.typesafe.config._

object Collector {

  case class CollectorMessage(actorRef: ActorRef, id: UUID, message: Any, stackTrace: Array[StackTraceElement])

  case class CollectorExceptionMessage(actorRef: ActorRef, id: UUID, exception: Exception)

}

trait Collector extends Actor {
  private[collector] def handleCollectorMessage(msg: CollectorMessage): Unit

  private[collector] def handleCollectorExceptionMessage(msg: CollectorExceptionMessage): Unit

  def filterStackTrace(stackTrace: Array[StackTraceElement]): Array[StackTraceElement] = {
    stackTrace.filter(el => !el.toString.startsWith("akka") && !el.toString.startsWith("scala"))
  }

  override def receive: Receive = {
    case msg: CollectorMessage =>
      handleCollectorMessage(msg)
    case msg: CollectorExceptionMessage =>
      handleCollectorExceptionMessage(msg)
  }
}


class FileCollector extends Collector {
  val fileWriter = new FileWriter("collector.txt", true)

  override private[collector] def handleCollectorMessage(msg: CollectorMessage): Unit = msg match {
    case CollectorMessage(actorRef, uuid, message, stackTrace) =>
      val niceStackTrace = filterStackTrace(stackTrace)
      fileWriter.write(String.format("===========\nMESSAGE\n===========\n%s\n%s\n%s\n%s\n",
        actorRef.toString, uuid.toString, message.toString, niceStackTrace.mkString("\n")))
      fileWriter.flush()
  }

  override private[collector] def handleCollectorExceptionMessage(msg: CollectorExceptionMessage): Unit = msg match {
    case CollectorExceptionMessage(actorRef: ActorRef, uuid: UUID, exception: Exception) =>
      val niceStackTrace = filterStackTrace(exception.getStackTrace)
      fileWriter.write(String.format("===========\nEXCEPTION\n===========\n%s\n%s\n%s\n%s\n",
        actorRef.toString, uuid.toString, exception.getClass.getCanonicalName, niceStackTrace.mkString("\n")))
      fileWriter.flush()
  }
}

object DatabaseCollector {
  def props(config: Config): Props = Props(classOf[DatabaseCollector], config)
}

class DatabaseCollector(config: Config) extends Collector {

  import akka_debugging.database._

  import scala.slick.driver.PostgresDriver.simple._

  lazy val database = Database.forDataSource(DatabaseUtils.getDataSource(config))
  val messages = TableQuery[CollectorDBMessages]
  val exceptions = TableQuery[CollectorDBExceptionMessages]

  override private[collector] def handleCollectorMessage(msg: CollectorMessage): Unit = msg match {
    case CollectorMessage(actorRef, uuid, message, stackTrace) =>
      val niceStackTrace = filterStackTrace(stackTrace)
      database.withSession { implicit session =>
        messages += CollectorDBMessage(uuid, actorRef.toString, message.toString, niceStackTrace.mkString("\n"))
      }
  }

  override private[collector] def handleCollectorExceptionMessage(msg: CollectorExceptionMessage): Unit = msg match {
    case CollectorExceptionMessage(actorRef: ActorRef, uuid: UUID, exception: Exception) =>
      val niceStackTrace = filterStackTrace(exception.getStackTrace)
      database.withSession { implicit session =>
        exceptions += CollectorDBExceptionMessage(uuid, actorRef.toString, exception.getClass.getCanonicalName,
          niceStackTrace.mkString("\n"))
      }
  }
}
