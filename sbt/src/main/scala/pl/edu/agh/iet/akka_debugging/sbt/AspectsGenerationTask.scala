package pl.edu.agh.iet.akka_debugging.sbt

import java.io.File

import com.typesafe.config.ConfigFactory

object AspectsGenerationTask {
  //TODO: Real implementation
  def generateAspects(resourcesDir: File, configurationFile: String): Unit = {
    val configFile = new File(s"${resourcesDir.getAbsolutePath}/$configurationFile")
    val config = ConfigFactory.parseFile(configFile)
    println(config)
    try {
      println(config.getString("akka.troll"))
    } catch {
      case e: Throwable => throw new IllegalArgumentException(s"Couldn't load configuration file $configurationFile", e)
    }
  }
}
