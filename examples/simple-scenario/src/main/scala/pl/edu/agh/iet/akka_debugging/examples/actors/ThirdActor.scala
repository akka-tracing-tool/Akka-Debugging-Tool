package pl.edu.agh.iet.akka_debugging.examples.actors

import akka.actor.Actor
import pl.edu.agh.iet.akka_debugging.DistributedStackTrace

class ThirdActor extends Actor with DistributedStackTrace {
  override def receive: Receive = {
    case a =>
      println(a)
  }
}
