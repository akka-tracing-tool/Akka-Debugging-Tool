package akka_debugging.monitor

import java.io.File
import java.util.{Collections, UUID}

import akka.actor.{Props, ActorSystem, Actor, ActorRef}
import com.typesafe.config.ConfigFactory
import akka_debugging.monitor.Collector.CollectorMessage
import org.aspectj.lang.{JoinPoint, ProceedingJoinPoint}
import org.aspectj.lang.annotation._

@Aspect
class MethodBang {
  val configFile = getClass.getClassLoader.getResource("remote_application.conf").getFile
  val config = ConfigFactory.parseFile(new File(configFile))
  val system = ActorSystem("RemoteSystem" , config)
  val collector = system.actorOf(Props[Collector], name="collector")

  //todo - what when scheduler send messages?
  @Pointcut("call(* akka.actor.ScalaActorRef.$bang(..)) && within(UnreliableWorker)") // && within(User) doesn't work
  def withinUnreliable(): Unit = {}

  @Before("akka_debugging.monitor.MethodBang.withinUnreliable() && args(msg,actorRef) && target(callee)")
  def aspectA(msg: AnyRef, actorRef: ActorRef, callee: ActorRef, joinPoint: JoinPoint): Unit = {
    println("bang method within UnreliableWorker: " + msg + " " + actorRef)
    collector ! CollectorMessage(callee, UUID.randomUUID(), msg, Thread.currentThread().getStackTrace.drop(2))
  }
}