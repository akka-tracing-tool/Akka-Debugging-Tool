import play.api.{Application, GlobalSettings}
import utils.DatabaseUtils

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
