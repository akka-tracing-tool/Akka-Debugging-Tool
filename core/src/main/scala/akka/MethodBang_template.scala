package akka

import java.io.File
import java.util.UUID

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation._
import pl.edu.agh.iet.akka_debugging.TracingActor
import pl.edu.agh.iet.akka_debugging.collector.Collector.{CollectorMessage, RelationMessage}
import pl.edu.agh.iet.akka_debugging.collector.DatabaseCollector

import scala.util.Random


//@Aspect
class MethodBang {
  val configFile = getClass.getClassLoader.getResource("remote_application.conf").getFile
  val config = ConfigFactory.parseFile(new File(configFile))
  val system = ActorSystem("RemoteSystem", config)
  val collector = system.actorOf(DatabaseCollector.props(config), name = "collector")

  @Pointcut("call(* pl.edu.agh.iet.akka_debugging.TracingActor$class.aroundReceive(..))")
  def aroundReceivePointcut(): Unit = {}

  @Around("akka.MethodBangAspect.aroundReceivePointcut()")
  def aspectAroundReceive(joinPoint: ProceedingJoinPoint): AnyRef = {
    val actor = joinPoint.getArgs()(0)
    val receive = joinPoint.getArgs()(1)
    val message = joinPoint.getArgs()(2) match {
      case msgWrapper: MessageWrapper =>
        actor match {
          case act: TracingActor => act.MessageWrapperId = msgWrapper.id
          case _ =>
        }
        collector ! CollectorMessage(msgWrapper.id, None, Some(actor.toString))
        msgWrapper.msg
      case msg =>
        msg
    }
    joinPoint.proceed(Array(actor, receive, message))
  }

  @Pointcut("call(* akka.actor.ScalaActorRef.$bang(..)) && <<<ACTORS>>>")
  def withinUnreliable(): Unit = {}

  @Around("akka.MethodBangAspect.withinUnreliable()")
  def aspectA(joinPoint: ProceedingJoinPoint): Any = {
    val msg = joinPoint.getArgs()(0)
    val sender = joinPoint.getArgs()(1)
    val actor = sender.asInstanceOf[RepointableActorRef].underlying.asInstanceOf[ActorCell].actor

    val uuid = UUID.randomUUID()
    val msgId = actor.asInstanceOf[TracingActor].MessageWrapperId
    collector ! RelationMessage(msgId, uuid)
    collector ! CollectorMessage(uuid, Some(actor.toString), None)

    joinPoint.proceed(Array[AnyRef](MessageWrapper(uuid, msg), sender))
  }

//  @AfterThrowing(pointcut = "execution(* akka.actor.Actor$class.aroundReceive(..))", throwing = "error")
//  def afterThrowingMethod(joinPoint: JoinPoint, error: Throwable): Unit = {
//    println("AFTER THROWING METHOD: \n" + error)
//  }
}
