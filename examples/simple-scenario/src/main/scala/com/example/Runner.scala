package com.example

import akka.actor.{ActorSystem, Props}
import akka_debugging.database.DatabaseUtils
import com.example.actors.{ThirdActor, SecondActor, FirstActor}

import scala.util.Random

object Runner {
  case class Message(random: Int)

  def main(args: Array[String]) {
    DatabaseUtils.init()
    val system = ActorSystem()
    val thirdActor = system.actorOf(Props[ThirdActor], "thirdActor")
    val secondActor = system.actorOf(SecondActor.props(thirdActor), "secondActor")
    val firstActor = system.actorOf(FirstActor.props(secondActor), "firstActor")

    for(x <- 1 to 10) {
      firstActor ! Message(Random.nextInt())
      println(x)
      Thread.sleep(300)
    }
  }
}
