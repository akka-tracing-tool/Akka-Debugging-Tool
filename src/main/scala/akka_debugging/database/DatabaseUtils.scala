package akka_debugging.database

import java.util.UUID

import com.typesafe.config._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

case class CollectorDBMessage(uuid: UUID,
                              actor: String,
                              message: String,
                              stackTrace: String,
                              id: Option[Long] = None)


case class CollectorDBExceptionMessage(uuid: UUID,
                                       actor: String,
                                       exception: String,
                                       stackTrace: String,
                                       id: Option[Long] = None)


object DatabaseUtils {
  val logger: Logger = LoggerFactory.getLogger(DatabaseUtils.getClass)

  import slick.backend.DatabaseConfig
  import slick.driver.JdbcProfile
  import slick.jdbc.meta._

  val dc = DatabaseConfig.forConfig[JdbcProfile]("database", ConfigFactory.load("remote_application.conf"))

  import dc.driver.api._

  class CollectorDBMessages(tag: Tag) extends Table[CollectorDBMessage](tag, "messages") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def uuid = column[UUID]("uuid")

    def actor = column[String]("actor")

    def message = column[String]("message")

    def stackTrace = column[String]("stackTrace")

    override def * = (uuid, actor, message, stackTrace, id.?) <>(CollectorDBMessage.tupled, CollectorDBMessage.unapply)

    def uuidIndex = index("messages_uuid_idx", uuid, unique = true)
  }

  class CollectorDBExceptionMessages(tag: Tag) extends Table[CollectorDBExceptionMessage](tag, "exceptions") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def uuid = column[UUID]("uuid")

    def actor = column[String]("actor")

    def exception = column[String]("exception")

    def stackTrace = column[String]("stackTrace")

    override def * =
      (uuid, actor, exception, stackTrace, id.?) <>(CollectorDBExceptionMessage.tupled, CollectorDBExceptionMessage.unapply)

    def uuidIndex = index("exceptions_uuid_idx", uuid, unique = true)
  }

  def init(): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val db = dc.db
    val messages = TableQuery[CollectorDBMessages]
    val exceptions = TableQuery[CollectorDBExceptionMessages]

    val f = db.run(MTable.getTables).map(
      (tablesVector: Vector[MTable]) => {
        val tables = tablesVector.toList.map((t: MTable) => t.name.name)
        var f = Seq[Future[Unit]]()
        if (!tables.contains("messages")) {
          f :+= db.run(messages.schema.create)
        }
        if (!tables.contains("exceptions")) {
          f :+= db.run(exceptions.schema.create)
        }
        Future.sequence(f)
      }
    )

    Await.result(f, 5 seconds)
  }
}
