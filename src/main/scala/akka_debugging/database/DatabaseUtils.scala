package akka_debugging.database

import com.typesafe.config._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

case class CollectorDBMessage(id: Int,
                            sender: String,
                            receiver: Option[String] = None)

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
    def receiver = column[String]("receiver", O.Nullable) // -.- postgres (deprecated)
    override def * = (id, sender, receiver.?) <>(CollectorDBMessage.tupled, CollectorDBMessage.unapply)
  }

  def init(): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val db = dc.db
    val messages = TableQuery[CollectorDBMessages]

    val f = db.run(MTable.getTables).map(
      (tablesVector: Vector[MTable]) => {
        val tables = tablesVector.toList.map((t: MTable) => t.name.name)
        var f = Seq[Future[Unit]]()
        if (!tables.contains("messages")) {
          f :+= db.run(messages.schema.create)
          logger.info("Creating table for messages...")
        }
        Future.sequence(f)
      }
    )

    Await.result(f, 5 seconds)
  }
}
