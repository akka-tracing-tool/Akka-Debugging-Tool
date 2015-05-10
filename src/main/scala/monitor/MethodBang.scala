package monitor

import org.aspectj.lang.{JoinPoint, ProceedingJoinPoint}
import org.aspectj.lang.annotation._

@Aspect
class MethodBang {

  @Before("execution(* akka.actor.ScalaActorRef.$bang(..))")
  def beforeBangMethod(joinPoint: JoinPoint): Unit = {
    println("beforeBangMethod: " + joinPoint.getSignature.getName)
  }

  @After("execution(* akka.actor.ScalaActorRef.$bang(..))")
  def afterBangMethod(joinPoint: JoinPoint): Unit = {
    println("afterBangMethod: " + joinPoint.getSignature.getName)
  }

  @AfterReturning(pointcut = "execution(int scala.util.Random.nextInt())", returning = "result")
  def afterReturningRandomNextInt(jointPoint: JoinPoint, result: Any) {
    println("afterReturningRandomNextInt: " + result.toString)
  }

  @AfterThrowing(pointcut = "execution(* User.aroundReceive(..))", throwing = "error")
  def afterThrowingMethod(joinPoint: JoinPoint, error: Throwable): Unit = {
    println("afterThrowingMethod: " + error.toString)
  }

  @Around("execution(* scala.util.Random.nextInt())")
  def aroundMethod(joinPoint: ProceedingJoinPoint): Any = {
    println("before Random.nextInt()")
    val result = joinPoint.proceed()
    println("after Random.nextInt()")
    result
  }
}