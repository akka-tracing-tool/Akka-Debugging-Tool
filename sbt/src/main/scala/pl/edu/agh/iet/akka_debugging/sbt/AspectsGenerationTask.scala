package pl.edu.agh.iet.akka_debugging.sbt

import com.typesafe.config.ConfigFactory

object AspectsGenerationTask {
  //TODO: Real implementation
  def generateAspects(configurationFile: String): Unit = {
    val config = ConfigFactory.load(configurationFile).atKey("test")
    println(config)
  }
}
