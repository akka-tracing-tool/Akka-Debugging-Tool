import akka.actor.{ActorSystem, Props}
import pl.edu.agh.iet.akka_debugging.database.DatabaseUtils

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Runner {
  def main(args: Array[String]) {
    Await.result(DatabaseUtils.init, 5 seconds)
    val system = ActorSystem()
    val unreliableWorkerRef = system.actorOf(Props[UnreliableWorker], "unreliableWorker")
    val userRef = system.actorOf(User.props(unreliableWorkerRef), "user")
  }
}
