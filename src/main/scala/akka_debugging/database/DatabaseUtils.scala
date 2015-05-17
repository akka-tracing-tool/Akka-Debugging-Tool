package akka_debugging.database

import java.util.UUID
import javax.sql.DataSource

import com.typesafe.config._
import org.apache.commons.dbcp.BasicDataSource

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.meta.MTable


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


class CollectorDBMessages(tag: Tag) extends Table[CollectorDBMessage](tag, "messages") {

  def id: Column[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc, O.NotNull)

  def uuid: Column[UUID] = column[UUID]("uuid", O.NotNull)

  def actor: Column[String] = column[String]("actor", O.NotNull, O.DBType("TEXT"))

  def message: Column[String] = column[String]("message", O.NotNull, O.DBType("TEXT"))

  def stackTrace: Column[String] = column[String]("stackTrace", O.NotNull, O.DBType("TEXT"))

  override def * = (uuid, actor, message, stackTrace, id.?) <>(CollectorDBMessage.tupled, CollectorDBMessage.unapply)

  def uuidIndex = index("messages_uuid_idx", uuid, unique = true)
}

class CollectorDBExceptionMessages(tag: Tag) extends Table[CollectorDBExceptionMessage](tag, "exceptions") {
  def id: Column[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc, O.NotNull)

  def uuid: Column[UUID] = column[UUID]("uuid", O.NotNull)

  def actor: Column[String] = column[String]("actor", O.NotNull, O.DBType("TEXT"))

  def exception: Column[String] = column[String]("exception", O.NotNull, O.DBType("TEXT"))

  def stackTrace: Column[String] = column[String]("stackTrace", O.NotNull, O.DBType("TEXT"))

  override def * =
    (uuid, actor, exception, stackTrace, id.?) <>(CollectorDBExceptionMessage.tupled, CollectorDBExceptionMessage.unapply)

  def uuidIndex = index("exceptions_uuid_idx", uuid, unique = true)
}


object DatabaseUtils {
  def getDataSource(config: Config): DataSource = {
    val ds = new BasicDataSource
    ds.setDriverClassName(config.getString("database.driver"))
    ds.setUsername(config.getString("database.username"))
    ds.setPassword(config.getString("database.password"))
    ds.setMaxActive(20)
    ds.setMaxIdle(10)
    ds.setInitialSize(10)
    ds.setUrl(config.getString("database.url"))
    ds
  }

  def init(config: Config): Unit = {
    lazy val database = Database.forDataSource(getDataSource(config))
    val messages = TableQuery[CollectorDBMessages]
    val exceptions = TableQuery[CollectorDBExceptionMessages]

    database.withSession { implicit session =>
      if (MTable.getTables("messages").list.isEmpty) {
        messages.ddl.create
      }

      if (MTable.getTables("exceptions").list.isEmpty) {
        exceptions.ddl.create
      }
    }
  }
}
