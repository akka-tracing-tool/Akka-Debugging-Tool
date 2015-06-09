package controllers

import models.{Message, MessageRelation}
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

    val f = for {
      messagesList <- messagesListFuture
      relationList <- relationListFuture
    } yield (messagesList, relationList)

    f.map({
      case (messagesList: List[Message], relationList: List[MessageRelation]) =>
        Ok(views.html.index(messagesList)(relationList))
    })
  }
}
