import akka.actor.{Props, ActorSystem}

object Runner {
  def main(args: Array[String]) {
    val system = ActorSystem()
    val unreliableWorkerRef = system.actorOf(Props[UnreliableWorker], "unreliableWorker")
    val userRef = system.actorOf(User.props(unreliableWorkerRef), "user")
  }
}
