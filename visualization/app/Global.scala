import com.typesafe.config.ConfigFactory
import pl.edu.agh.iet.akka_tracing.database.DatabaseUtils
import play.api.libs.concurrent.Execution.Implicits._
import play.api.{Application, GlobalSettings}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Global extends GlobalSettings {
  val logger = play.api.Logger.logger

  override def onStart(app: Application): Unit = {
    super.onStart(app)
    logger.info("Checking for database tables...")
    val databaseUtils = new DatabaseUtils(ConfigFactory.load)
    Await.result(databaseUtils.init, 5 seconds)
  }
}
