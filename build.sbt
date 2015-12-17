import pl.edu.agh.iet.akka_tracing.sbt.AkkaTracingPlugin

val org = "pl.edu.agh.iet"
val appVersion = "0.0.1-SNAPSHOT"

organization := org

name := "akka-tracing-examples"

version := appVersion

scalaVersion := "2.11.7"

val AkkaVersion = "2.3.9"
val AspectJVersion = "1.7.2"

lazy val root = (project in file(".")).aggregate(oneToMany, simpleScenario)

val libDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.xerial" % "sqlite-jdbc" % "3.8.11.1",
  "com.zaxxer" % "HikariCP-java6" % "2.3.3",
  "org.aspectj" % "aspectjweaver" % AspectJVersion,
  "org.aspectj" % "aspectjrt" % AspectJVersion
)

lazy val oneToMany = (project in file("one-to-many")).enablePlugins(AkkaTracingPlugin).settings(
  name := "one-to-many-example",
  libraryDependencies ++= libDependencies
).dependsOn(uri("https://github.com/akka-tracing-tool/akka-tracing-core.git"))

lazy val simpleScenario = (project in file("simple-scenario")).enablePlugins(AkkaTracingPlugin).settings(
  name := "simple-scenario-example",
  libraryDependencies ++= libDependencies
).dependsOn(uri("https://github.com/akka-tracing-tool/akka-tracing-core.git"))
