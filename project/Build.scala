import play.sbt.PlayImport._
import play.sbt.PlayScala
import play.sbt.routes.RoutesKeys._
import sbt.Keys._
import sbt._

object Build extends Build {

  val org = "pl.edu.agh.iet.akka-debugging-tool"
  val appVersion = "0.0.1-SNAPSHOT"

  val ScalaVersion = "2.11.7"
  val ScalaTestVersion = "2.2.5"
  val Slf4jVersion = "1.7.12"
  val SlickVersion = "3.0.0"
  val ConfigVersion = "1.3.0"
  val AkkaVersion = "2.3.9"

  val rootSettings = Seq(
    version := appVersion,
    organization := org,
    scalaVersion := ScalaVersion,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
      "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
      "org.slf4j" % "slf4j-api" % Slf4jVersion,
      "com.typesafe" % "config" % ConfigVersion,
      "com.typesafe.slick" %% "slick" % SlickVersion,
      "org.slf4j" % "log4j-over-slf4j" % Slf4jVersion % "test",
      "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
    )
  )

  lazy val root = Project("akka-debugging-tool", file("."))
    .settings(rootSettings: _*)
    .settings(name := "akka-debugging-tool")
    .aggregate(
      core,
      visualization
    )

  lazy val core = Project("akka-debugging-tool-core", file("core"))
    .settings(rootSettings: _*)
    .settings(
      name := "akka-debugging-tool-core",
      libraryDependencies += "org.aspectj" % "aspectjweaver" % "1.7.2",
      libraryDependencies += "org.aspectj" % "aspectjrt" % "1.7.2"
    )

  lazy val visualization = Project("akka-debugging-tool-visualization", file("visualization"))
    .enablePlugins(PlayScala)
    .settings(rootSettings: _*)
    .settings(
      name := "akka-debugging-tool-visualization",
//      scalaVersion := "2.11.6",
      libraryDependencies ++= Seq(
        jdbc,
        cache,
        ws,
        specs2 % Test,
        // Database connections
        "com.typesafe.slick" %% "slick" % "3.0.0",
        "postgresql" % "postgresql" % "9.1-901.jdbc4"
      ),
      resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
      routesGenerator := InjectedRoutesGenerator
    )
    .dependsOn(core)
}