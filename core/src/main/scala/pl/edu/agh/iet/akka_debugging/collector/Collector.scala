package pl.edu.agh.iet.akka_debugging.collector

import akka.actor.{Actor, Props}
import com.typesafe.config._
import pl.edu.agh.iet.akka_debugging.database.DatabaseUtils.{CollectorDBMessages, CollectorDBMessagesRelation}
import pl.edu.agh.iet.akka_debugging.database.{CollectorDBMessage, CollectorDBMessageRelation}

import scala.concurrent.Await
import scala.concurrent.duration._

object Collector {
  case class CollectorMessage(id: Int, sender: Option[String], receiver: Option[String])
  case class RelationMessage(id1: Int, id2: Int)
}

trait Collector extends Actor {
  import Collector._
  private[collector] def handleCollectorMessage(msg: CollectorMessage): Unit
  private[collector] def handleRelationMessage(msg: RelationMessage): Unit
  private[collector] def filterStackTrace(stackTrace: Array[StackTraceElement]): Array[StackTraceElement] = {
    stackTrace.filter(el => !el.toString.startsWith("akka") && !el.toString.startsWith("scala"))
  }

  override def receive: Receive = {
    case msg: CollectorMessage =>
      handleCollectorMessage(msg)
    case msg: RelationMessage =>
      handleRelationMessage(msg)
  }
}
//class FileCollector extends Collector {
//
//  import Collector._
//
//  val fileWriter = new FileWriter("collector.txt", true)
//
//  override private[collector] def handleCollectorMessage(msg: CollectorMessage): Unit = msg match {
//    case CollectorMessage(id, sender, receiver) =>
//      fileWriter.write(String.format("===========\nMESSAGE\n===========\n%s\n%s\n%s\n%s\n",
//        actorRef.toString, uuid.toString, message.toString, niceStackTrace.mkString("\n")))
//      fileWriter.flush()
//  }
//}

object DatabaseCollector {
  def props(config: Config): Props = Props(classOf[DatabaseCollector], config)
}

class DatabaseCollector(config: Config) extends Collector {
  import Collector._
  import slick.backend.DatabaseConfig
  import slick.driver.JdbcProfile

  val dc = DatabaseConfig.forConfig[JdbcProfile]("database", config)

  import dc.driver.api._

  val db = dc.db
  val messages = TableQuery[CollectorDBMessages]
  val relations = TableQuery[CollectorDBMessagesRelation]

  override private[collector] def handleCollectorMessage(msg: CollectorMessage): Unit = msg match {
    case CollectorMessage(id, sender, None) =>
      val f = db.run(messages += CollectorDBMessage(id, sender.get, None))
      Await.result(f, 5 seconds)
    case CollectorMessage(id, None, receiver) =>
      val f = db.run(messages.filter(_.id === id).map(_.receiver).update(receiver))
      Await.result(f, 5 seconds)
  }

  override private[collector] def handleRelationMessage(msg: RelationMessage): Unit = msg match {
    case RelationMessage(id1, id2) =>
      val f = db.run(relations += CollectorDBMessageRelation(id1, id2))
      Await.result(f, 5 seconds)
  }
}
