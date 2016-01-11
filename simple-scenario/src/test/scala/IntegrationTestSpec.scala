import java.io.File

import com.typesafe.config.ConfigFactory
import org.scalatest.FlatSpec
import pl.edu.agh.iet.akka_tracing.database.DatabaseUtils

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class IntegrationTestSpec extends FlatSpec {
  "The Akka Tracing library" should "collect proper traces during an actor system run" in {
    val process = new ProcessBuilder("sbt", "initDatabase", "cleanDatabase", "run").start()
    Thread.sleep(60000)
    process.destroyForcibly().waitFor()

    val dbConfigFile = new File(getClass.getResource("/database.conf").toURI)
    val dbUtils = new DatabaseUtils(ConfigFactory.parseFile(dbConfigFile))
    val dc = dbUtils.getDatabaseConfig
    val db = dc.db

    import dc.driver.api._

    val countMessagesQuery = sql"""select count(*) from "messages"""".as[Int]
    val countRelationQuery = sql"""select count(*) from "relation"""".as[Int]
    val messagesRowsCount = Await.result(db.run(countMessagesQuery), 1 seconds).head
    val relationRowsCount = Await.result(db.run(countRelationQuery), 1 seconds).head

    assert(messagesRowsCount === 20)
    assert(relationRowsCount === 20)

    new ProcessBuilder("sbt", "cleanDatabase").start().waitFor()
  }
}
