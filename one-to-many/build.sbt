import pl.edu.agh.iet.akka_tracing.sbt.AkkaTracingPlugin

val org = "pl.edu.agh.iet"
val appVersion = "0.1"

organization := org

name := "akka-tracing-one-to-many-example"

version := appVersion

scalaVersion := "2.11.11"

val AkkaVersion = "2.4.17"
val Slf4jVersion = "1.7.24"

val libDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.xerial" % "sqlite-jdbc" % "3.16.1",
  "org.slf4j" % "slf4j-api" % Slf4jVersion,
  "org.slf4j" % "slf4j-simple" % Slf4jVersion
)

libraryDependencies ++= libDependencies

lazy val oneToMany = (project in file(".")).enablePlugins(AkkaTracingPlugin)
