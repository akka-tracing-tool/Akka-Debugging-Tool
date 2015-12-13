package pl.edu.agh.iet.akka_tracing.examples.actors

import akka.actor.{Actor, ActorRef, Props}
import pl.edu.agh.iet.akka_tracing.TracedActor

object SecondActor {
  def props(actorRef: ActorRef): Props = Props(classOf[SecondActor], actorRef)
}

class SecondActor(val actorRef: ActorRef) extends Actor with TracedActor {
  override def receive: Receive = {
    case a =>
      actorRef ! a
  }
}
