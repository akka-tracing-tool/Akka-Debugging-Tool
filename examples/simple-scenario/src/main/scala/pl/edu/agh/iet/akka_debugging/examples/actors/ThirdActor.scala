package pl.edu.agh.iet.akka_debugging.examples.actors

import akka.actor.Actor
import pl.edu.agh.iet.akka_debugging.TracedActor

class ThirdActor extends Actor with TracedActor {
  override def receive: Receive = {
    case a =>
      println(a)
  }
}
