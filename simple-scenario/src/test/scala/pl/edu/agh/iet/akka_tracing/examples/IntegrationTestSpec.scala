package pl.edu.agh.iet.akka_tracing.examples

import java.io.File

import com.typesafe.config.ConfigFactory
import org.scalatest.FlatSpec
import pl.edu.agh.iet.akka_tracing.utils.DatabaseUtils

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

    import dbUtils._
    import dc.profile.api._

    val countSenderMessagesQuery = senderMessages.size.result
    val countReceiverMessagesQuery = receiverMessages.size.result
    val countRelationQuery = relations.size.result
    val senderMessagesRowsCount = Await.result(db.run(countSenderMessagesQuery), 1 second)
    val receiverMessagesRowsCount = Await.result(db.run(countReceiverMessagesQuery), 1 second)
    val relationRowsCount = Await.result(db.run(countRelationQuery), 1 second)

    assert(senderMessagesRowsCount === 20)
    assert(receiverMessagesRowsCount === 20)
    assert(relationRowsCount === 20)

    new ProcessBuilder("sbt", "cleanDatabase").start().waitFor()
  }
}
