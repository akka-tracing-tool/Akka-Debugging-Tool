import akka.actor.{ActorSystem, Props}
import akka_debugging.collector._
import akka_debugging.database.DatabaseUtils
import com.typesafe.config._

object Runner {
  def main(args: Array[String]) {
    val configFile = "config.conf"
    val config = ConfigFactory.load(configFile)
    DatabaseUtils.init(config)
    val system = ActorSystem()
    val unreliableWorkerRef = system.actorOf(Props[UnreliableWorker], "unreliableWorker")
    val userRef = system.actorOf(User.props(unreliableWorkerRef), "user")
    //    val collectorRef = system.actorOf(Props[FileCollector], "collector")
    val collectorRef = system.actorOf(DatabaseCollector.props(config), "collector")
  }
}
