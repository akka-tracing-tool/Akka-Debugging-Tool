import akka.actor.{ActorSystem, Props}
import pl.edu.agh.iet.akka_debugging.database.DatabaseUtils

object Runner {
  def main(args: Array[String]) {
    DatabaseUtils.init()
    val system = ActorSystem()
    val unreliableWorkerRef = system.actorOf(Props[UnreliableWorker], "unreliableWorker")
    val userRef = system.actorOf(User.props(unreliableWorkerRef), "user")
  }
}
