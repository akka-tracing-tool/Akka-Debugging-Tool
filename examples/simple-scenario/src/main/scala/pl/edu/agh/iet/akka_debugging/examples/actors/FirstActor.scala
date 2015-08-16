package pl.edu.agh.iet.akka_debugging.examples.actors

import akka.actor.{Actor, ActorRef, Props}
import pl.edu.agh.iet.akka_debugging.DistributedStackTrace

object FirstActor {
  def props(actorRef: ActorRef): Props = Props(classOf[FirstActor], actorRef)
}

class FirstActor(val actorRef: ActorRef) extends Actor with DistributedStackTrace {
  override def receive: Receive = {
    case a =>
      actorRef ! a
  }
}
