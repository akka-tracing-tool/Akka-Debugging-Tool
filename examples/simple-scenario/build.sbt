lazy val actors = SettingKey[Seq[String]]("Actors.")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "pl.edu.agh.iet" % "akka-debugging-tool-core_2.11" % "0.0.1-SNAPSHOT" % Runtime,
  "pl.edu.agh.iet" % "akka-debugging-tool-core_2.11" % "0.0.1-SNAPSHOT"
)

javaOptions += "-javaagent:" + System.getProperty("user.home") + "/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-1.7.2.jar"

fork := true

import pl.edu.agh.iet.akka_debugging.sbt.AkkaDebuggingPlugin.Settings._

compile in Compile <<= (compile in Compile) dependsOn (generateAspectsTask in Compile)
