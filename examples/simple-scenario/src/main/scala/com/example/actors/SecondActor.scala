package com.example.actors

import akka.actor.{Props, ActorRef, Actor}
import akka_debugging.DistributedStackTrace

object SecondActor {
  def props(actorRef: ActorRef): Props = Props(classOf[SecondActor], actorRef)
}

class SecondActor(val actorRef: ActorRef) extends Actor with DistributedStackTrace {
  override def receive: Receive = {
    case a =>
      actorRef ! a
  }
}
