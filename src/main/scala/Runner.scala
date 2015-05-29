import akka.actor.{ActorSystem, Props}
import akka_debugging.database.DatabaseUtils

object Runner {
  def main(args: Array[String]) {
    DatabaseUtils.init()
    val system = ActorSystem()
    val unreliableWorkerRef = system.actorOf(Props[UnreliableWorker], "unreliableWorker")
    val userRef = system.actorOf(User.props(unreliableWorkerRef), "user")
  }
}
