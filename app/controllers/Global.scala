package controllers

import play.api.{Application, GlobalSettings}
import utils.DatabaseUtils

import scala.concurrent.Await
import scala.concurrent.duration._

object Global extends GlobalSettings {
  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Await.result(DatabaseUtils.init, 5 seconds)
  }
}
