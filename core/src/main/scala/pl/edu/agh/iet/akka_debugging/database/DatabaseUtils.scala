package pl.edu.agh.iet.akka_debugging.database

import com.typesafe.config._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{ExecutionContext, Future}

case class CollectorDBMessage(id: Int,
                              sender: String,
                              receiver: Option[String] = None) {
  def toJsonString: String = "{\"id\":" + id + ", \"sender\": \"" + sender + "\", \"receiver\": \"" + receiver + "\"}"
}

case class CollectorDBMessagesRelation(id1: Int, id2: Int) {
  def toJsonString: String = "{\"id1\":" + id1 + ", \"id2\": " + id2 + "}"
}

object DatabaseUtils {
  val logger: Logger = LoggerFactory.getLogger(DatabaseUtils.getClass)

  import slick.backend.DatabaseConfig
  import slick.driver.JdbcProfile
  import slick.jdbc.meta._

  val dc = DatabaseConfig.forConfig[JdbcProfile]("database", ConfigFactory.load("remote_application.conf"))

  import dc.driver.api._

  class CollectorDBMessages(tag: Tag) extends Table[CollectorDBMessage](tag, "messages") {
    def id = column[Int]("id", O.PrimaryKey)
    def sender = column[String]("sender")
    def receiver = column[Option[String]]("receiver")
    override def * = (id, sender, receiver) <> (CollectorDBMessage.tupled, CollectorDBMessage.unapply)
  }

  class CollectorDBMessagesRelations(tag: Tag) extends Table[CollectorDBMessagesRelation](tag, "relation") {
    def id1 = column[Int]("id1")
    def id2 = column[Int]("id2")
    def pk = primaryKey("pk", (id1, id2))
    override def * = (id1, id2) <> (CollectorDBMessagesRelation.tupled, CollectorDBMessagesRelation.unapply)
  }

  def init(implicit ex: ExecutionContext): Future[Unit] = {
    val db = dc.db
    val messages = TableQuery[CollectorDBMessages]
    val relation = TableQuery[CollectorDBMessagesRelations]
    val f = db.run(MTable.getTables).map(
      (tablesVector: Vector[MTable]) => {
        val tables = tablesVector.toList.map((t: MTable) => t.name.name)
        var f = Seq[Future[Unit]]()
        if (!tables.contains("messages")) {
          f :+= db.run(messages.schema.create)
          logger.info("Creating table for messages...")
        }
        if (!tables.contains("relation")) {
          f :+= db.run(relation.schema.create)
          logger.info("Creating table for relations...")
        }
        Future.sequence(f)
      }
    )
    f.flatMap[Unit]((x: Future[Seq[Unit]]) => Future {
      logger.info("Done")
    })
  }

  def getDatabaseConfig: DatabaseConfig[JdbcProfile] = {
    dc
  }
}
