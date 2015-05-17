import akka.actor.{ActorSystem, Props}
import akka_debugging.collector.Collector

object Runner {
  def main(args: Array[String]) {
    val system = ActorSystem()
    val unreliableWorkerRef = system.actorOf(Props[UnreliableWorker], "unreliableWorker")
    val userRef = system.actorOf(User.props(unreliableWorkerRef), "user")
    val collectorRef = system.actorOf(Props[Collector], "collector")
  }
}
