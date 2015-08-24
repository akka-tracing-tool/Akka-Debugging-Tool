package pl.edu.agh.iet.akka_debugging.sbt

object AkkaDebuggingPlugin extends AutoPlugin {

  object Settings {
    lazy val generateAspectsTask = TaskKey[Unit]("generateAspects",
      "Reads configuration from file and generates aspects")
    lazy val aspectsConfigurationFile = SettingKey[String]("aspectsConfigurationFile",
      "Specifies file to read configuration from; file must be reachable from classpath")
  }

  import Settings._

  override lazy val projectSettings = Seq(
    aspectsConfigurationFile := "akka_debugging.conf",
    generateAspectsTask := {
      println("YEAH")
      generateAspects(aspectsConfigurationFile.value)
    }
  )

}
