import akka.actor.{ActorSystem, Props}
import akka_debugging.database.DatabaseUtils
import com.typesafe.config._

object Runner {
  def main(args: Array[String]) {
    val configFile = "remote_application.conf"
    val config = ConfigFactory.load(configFile)
    DatabaseUtils.init()
    val system = ActorSystem()
    val unreliableWorkerRef = system.actorOf(Props[UnreliableWorker], "unreliableWorker")
    val userRef = system.actorOf(User.props(unreliableWorkerRef), "user")
  }
}
