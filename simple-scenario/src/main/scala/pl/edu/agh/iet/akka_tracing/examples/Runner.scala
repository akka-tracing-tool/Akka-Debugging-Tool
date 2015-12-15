package pl.edu.agh.iet.akka_tracing.examples

import akka.actor.{ActorSystem, Props}
import pl.edu.agh.iet.akka_tracing.examples.actors.{FirstActor, SecondActor, ThirdActor}

import scala.util.Random

object Runner {
  case class Message(random: Int)

  def main(args: Array[String]) {
    val system = ActorSystem()
    val thirdActor = system.actorOf(Props[ThirdActor], "thirdActor")
    val secondActor = system.actorOf(SecondActor.props(thirdActor), "secondActor")
    val firstActor = system.actorOf(FirstActor.props(secondActor), "firstActor")

    for(x <- 1 to 10) {
      firstActor ! Message(Random.nextInt())
      println(x)
      Thread.sleep(300)
    }
    system.shutdown()
  }
}
