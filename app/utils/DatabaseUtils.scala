package utils

import com.typesafe.config.ConfigFactory
import models.{Message, MessageRelation}
import org.slf4j.LoggerFactory

import scala.concurrent.Future

object DatabaseUtils {
  val logger = LoggerFactory.getLogger(getClass)

  import slick.backend.DatabaseConfig
  import slick.driver.JdbcProfile
  import slick.jdbc.meta._

  val dc = DatabaseConfig.forConfig[JdbcProfile]("database", ConfigFactory.load())

  import dc.driver.api._

  class Messages(tag: Tag) extends Table[Message](tag, "messages") {
    def id = column[Int]("id", O.PrimaryKey)

    def sender = column[String]("sender")

    def receiver = column[String]("receiver")

    override def * = (id, sender, receiver) <>(Message.tupled, Message.unapply)
  }

  class Relation(tag: Tag) extends Table[MessageRelation](tag, "relation") {
    def id1 = column[Int]("id1")

    def id2 = column[Int]("id2")

    override def * = (id1, id2) <>(MessageRelation.tupled, MessageRelation.unapply)
  }

  def init(): Future[Unit] = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val db = dc.db
    val messages = TableQuery[Messages]
    val relation = TableQuery[Relation]

    db.run(MTable.getTables).map(
      (tablesVector: Vector[MTable]) => {
        val tables = tablesVector.toList.map((t: MTable) => t.name.name)
        var f = Seq[Future[Unit]]()
        if (!tables.contains("messages")) {
          f :+= db.run(messages.schema.create)
          logger.info("Creating table for messages...")
        }
        if (!tables.contains("relation")) {
          f :+= db.run(relation.schema.create)
          logger.info("Creating table for relation...")
        }
        Future.sequence(f)
      }
    )
  }

  def getDatabaseConfig: DatabaseConfig[JdbcProfile] = {
    dc
  }
}
