import pl.edu.agh.iet.akka_tracing.sbt.AkkaTracingPlugin

val org = "pl.edu.agh.iet"
val appVersion = "0.0.2"

organization := org

name := "akka-tracing-simple-scenario-example"

version := appVersion

scalaVersion := "2.11.7"

val AkkaVersion = "2.3.9"
val Slf4jVersion = "1.7.12"

val libDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.xerial" % "sqlite-jdbc" % "3.8.11.1",
  "org.slf4j" % "slf4j-api" % Slf4jVersion,
  "org.slf4j" % "slf4j-simple" % Slf4jVersion,
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

libraryDependencies ++= libDependencies

lazy val simpleScenario = (project in file(".")).enablePlugins(AkkaTracingPlugin)
