package monitor

import akka.actor.ActorRef
import org.aspectj.lang.{JoinPoint, ProceedingJoinPoint}
import org.aspectj.lang.annotation._

@Aspect
class MethodBang {

//  @Before("execution(* akka.actor.ScalaActorRef.$bang(..)) && args(msg,..)")
//  def beforeBangMethod(joinPoint: JoinPoint): Unit = {
//    println("beforeBangMethod: " + joinPoint.getSignature.getName)
//  }
//
//  @After("execution(* akka.actor.ScalaActorRef.$bang(..))")
//  def afterBangMethod(joinPoint: JoinPoint): Unit = {
//    println("afterBangMethod: " + joinPoint.getSignature.getName)
//  }
//
//  @AfterReturning(pointcut = "execution(int scala.util.Random.nextInt())", returning = "result")
//  def afterReturningRandomNextInt(jointPoint: JoinPoint, result: Any) {
//    println("afterReturningRandomNextInt: " + result.toString)
//  }
//
//  @AfterThrowing(pointcut = "execution(* User.aroundReceive(..))", throwing = "error")
//  def afterThrowingMethod(joinPoint: JoinPoint, error: Throwable): Unit = {
//    println("afterThrowingMethod: " + error.toString)
//  }
//
//  @Around("execution(* scala.util.Random.nextInt())")
//  def aroundMethod(joinPoint: ProceedingJoinPoint): Any = {
//    println("before Random.nextInt()")
//    val result = joinPoint.proceed()
//    println("after Random.nextInt()")
//    result
//  }

  //todo - what when scheduler send messages?
  @Pointcut("call(* akka.actor.ScalaActorRef.$bang(..)) && within(UnreliableWorker)") // && within(User) doesn't work
  def withinUnreliable(): Unit = {}

  @Before("monitor.MethodBang.withinUnreliable() && args(msg,actorRef)")
  def aspectA(msg: AnyRef, actorRef: ActorRef): Unit = {
    println("bang method within UnreliableWorker: " + msg + " " + actorRef)
  }

//  @Pointcut("execution(* akka.actor.ScalaActorRef.$bang(..))")
//  def bangMethodPointcut(): Unit = {}
//
//  @Around("monitor.MethodBang.bangMethodPointcut() && args(msg,actorRef)")
//  def aspect(joinPoint: ProceedingJoinPoint, msg: AnyRef, actorRef: ActorRef): Unit = {
////    println("ASPECT: " + joinPoint.getSignature.toLongString)
////    println("ASPECTmsg: " + msg)
//    if(actorRef!=null) {
//      actorRef.getClass.getInterfaces.foreach(println)
////      println("ASPECTref: " + actorRef.getClass.getInterfaces)
//    }
////    println("ASPECTref: " + actorRef.getClass.getInterfaces)
//    joinPoint.proceed()
//  }
}