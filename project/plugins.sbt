logLevel := Level.Warn

addSbtPlugin("pl.edu.agh.iet" % "akka-tracing-sbt" % "0.0.1-SNAPSHOT")

lazy val plugins = project in file(".") dependsOn uri("https://github.com/akka-tracing-tool/akka-tracing-sbt.git")
