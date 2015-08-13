package com.example.actors

import akka.actor.Actor
import akka_debugging.DistributedStackTrace

class ThirdActor extends Actor with DistributedStackTrace {
  override def receive: Receive = {
    case a =>
      println(a)
  }
}
