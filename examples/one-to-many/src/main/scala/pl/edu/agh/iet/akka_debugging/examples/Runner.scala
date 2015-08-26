package pl.edu.agh.iet.akka_debugging.examples

import akka.actor.{ActorSystem, Props}
import pl.edu.agh.iet.akka_debugging.database.DatabaseUtils
import pl.edu.agh.iet.akka_debugging.examples.actors.{FirstActor, SecondActor}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Random

object Runner {
  case class Message(random: Int)

  def main(args: Array[String]) {
    Await.result(DatabaseUtils.init, 5 seconds)
    val system = ActorSystem()
    val secondActors = system.actorOf(Props[SecondActor], "secondActor1") ::
      system.actorOf(Props[SecondActor], "secondActor2") ::
      system.actorOf(Props[SecondActor], "secondActor3") ::
      system.actorOf(Props[SecondActor], "secondActor4") :: Nil

    val firstActor = system.actorOf(FirstActor.props(secondActors), "firstActor")

    for(x <- 1 to 10) {
      firstActor ! Message(Random.nextInt())
      println(x)
      Thread.sleep(300)
    }
    Thread.sleep(5000)
    system.shutdown()
    System.exit(0)
  }
}
