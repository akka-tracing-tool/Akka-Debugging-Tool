package pl.edu.agh.iet.akka_tracing.examples.actors

import akka.actor.Actor
import pl.edu.agh.iet.akka_tracing.TracedActor

class SecondActor extends Actor with TracedActor {
  override def receive: Receive = {
    case a =>
      println(a)
  }
}
