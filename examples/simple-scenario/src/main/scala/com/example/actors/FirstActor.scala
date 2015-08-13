package com.example.actors

import akka.actor.{Props, ActorRef, Actor}
import akka_debugging.DistributedStackTrace

object FirstActor {
  def props(actorRef: ActorRef): Props = Props(classOf[FirstActor], actorRef)
}

class FirstActor(val actorRef: ActorRef) extends Actor with DistributedStackTrace {
  override def receive: Receive = {
    case a =>
      actorRef ! a
  }
}
