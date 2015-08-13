package controllers

import pl.edu.agh.iet.akka_debugging.database.DatabaseUtils
import pl.edu.agh.iet.akka_debugging.database.DatabaseUtils.{CollectorDBMessages, CollectorDBMessagesRelations}
import play.api.mvc._

class MainController extends Controller {
  val dc = DatabaseUtils.getDatabaseConfig

  import dc.driver.api._

  val db = dc.db
  val messages = TableQuery[CollectorDBMessages]
  val relation = TableQuery[CollectorDBMessagesRelations]

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