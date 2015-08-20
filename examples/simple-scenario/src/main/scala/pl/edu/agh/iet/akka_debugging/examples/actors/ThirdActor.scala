package pl.edu.agh.iet.akka_debugging.examples.actors

import akka.actor.Actor
import pl.edu.agh.iet.akka_debugging.TracingActor

class ThirdActor extends Actor with TracingActor {
  override def receive: Receive = {
    case a =>
      println(a)
  }
}
