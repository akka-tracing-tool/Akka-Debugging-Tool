package akka_debugging.monitor

import java.io.File
import java.util.UUID

import akka.actor.{Props, ActorSystem, Actor, ActorRef}
import akka.actor.{ActorRef, ActorSystem}
import akka_debugging.collector.Collector.CollectorMessage
import akka_debugging.collector.DatabaseCollector
import com.typesafe.config.ConfigFactory
import org.aspectj.lang.{ProceedingJoinPoint, JoinPoint}
import org.aspectj.lang.annotation._

import scala.util.Random

case class MessageWrapper(id: Int, msg: AnyRef)

@Aspect
class MethodBang {
  val configFile = getClass.getClassLoader.getResource("remote_application.conf").getFile
  val config = ConfigFactory.parseFile(new File(configFile))
  val system = ActorSystem("RemoteSystem", config)
  val collector = system.actorOf(DatabaseCollector.props(config), name = "collector")

  //todo it only works for trait DistributedStackTrace
  @Pointcut("call(* akka_debugging.DistributedStackTrace$class.aroundReceive(..))")
  def aroundReceivePointcut(): Unit = {}

  @Around("akka_debugging.monitor.MethodBang.aroundReceivePointcut()")
  def aspectAroundReceive(joinPoint: ProceedingJoinPoint): AnyRef = {
    val actor = joinPoint.getArgs()(0)
    val receive = joinPoint.getArgs()(1)
    val msgWrapper = joinPoint.getArgs()(2).asInstanceOf[MessageWrapper]

    println("RECEIVED: " + msgWrapper.id + " -> " + msgWrapper.msg)

    val newArgsArray = Array(actor, receive, msgWrapper.msg)
    joinPoint.proceed(newArgsArray)
  }

  //todo - what when scheduler send messages?
  @Pointcut("call(* akka.actor.ScalaActorRef.$bang(..)) && within(UnreliableWorker)") // && within(User) doesn't work
  def withinUnreliable(): Unit = {}

  @Around("akka_debugging.monitor.MethodBang.withinUnreliable() && args(msg,actorRef)")
  def aspectA(msg: AnyRef, actorRef: ActorRef, joinPoint: ProceedingJoinPoint): Any = {
    val random = Random.nextInt()
    println("SENT: " + random + " -> " + joinPoint.getArgs()(0))

    val newArgsArray = Array[AnyRef](MessageWrapper(random, msg), actorRef)
    joinPoint.proceed(newArgsArray)

//    println("bang method within UnreliableWorker: " + msg + " " + actorRef)
//    collector ! CollectorMessage(callee, UUID.randomUUID(), msg, Thread.currentThread().getStackTrace.drop(2))
  }

  @AfterThrowing(pointcut = "execution(* akka.actor.Actor$class.aroundReceive(..))", throwing = "error")
  def afterThrowingMethod(joinPoint: JoinPoint, error: Throwable): Unit = {
    println("AFTER THROWING METHOD: \n" + error)
  }
}