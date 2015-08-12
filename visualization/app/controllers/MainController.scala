package controllers

import play.api.mvc._
import utils.DatabaseUtils
import utils.DatabaseUtils.{Messages, Relation}

class MainController extends Controller {
  val dc = DatabaseUtils.getDatabaseConfig

  import dc.driver.api._

  val db = dc.db
  val messages = TableQuery[Messages]
  val relation = TableQuery[Relation]

  def index = Action.async {
    import play.api.libs.concurrent.Execution.Implicits._

    val messagesListFuture = db.run(messages.to[List].result)
    val relationListFuture = db.run(relation.to[List].result)

    for {
      messagesList <- messagesListFuture
      relationList <- relationListFuture
    } yield Ok(views.html.index(messagesList)(relationList))
  }
}
