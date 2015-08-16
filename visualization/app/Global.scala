import pl.edu.agh.iet.akka_debugging.database.DatabaseUtils
import play.api.libs.concurrent.Execution.Implicits._
import play.api.{Application, GlobalSettings}

import scala.concurrent.Await
import scala.concurrent.duration._

object Global extends GlobalSettings {
  val logger = play.api.Logger.logger

  override def onStart(app: Application): Unit = {
    super.onStart(app)
    logger.info("Checking for database tables...")
    Await.result(DatabaseUtils.init, 5 seconds)
  }
}
