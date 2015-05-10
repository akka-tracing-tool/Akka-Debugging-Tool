package monitor

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.{Around, Aspect}

@Aspect
class MethodBang {
  @Around("execution(* akka.actor.ScalaActorRef.*(..))")
  def bangMethod(joinPoint: ProceedingJoinPoint): Any = {
    println("SIEMANDERO")
    joinPoint.proceed()
  }
}