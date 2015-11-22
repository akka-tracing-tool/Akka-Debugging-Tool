import pl.edu.agh.iet.akka_debugging.sbt.AkkaDebuggingPlugin
import play.sbt.PlayImport._
import play.sbt.PlayScala
import play.sbt.routes.RoutesKeys._
import sbt.Keys._
import sbt._

object Build extends Build {
  val org = "pl.edu.agh.iet"
  val appVersion = "0.0.1-SNAPSHOT"

  val ScalaVersion = "2.11.7"
  val ScalaTestVersion = "2.2.5"
  val Slf4jVersion = "1.7.12"
  val SlickVersion = "3.0.0"
  val ConfigVersion = "1.3.0"
  val AkkaVersion = "2.3.9"
  val AspectJVersion = "1.7.2"

  val rootSettings = Seq(
    version := appVersion,
    organization := org,
    scalaVersion := ScalaVersion,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
      "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
      "com.typesafe" % "config" % ConfigVersion,
      "com.typesafe.slick" %% "slick" % SlickVersion,
      "org.slf4j" % "slf4j-api" % Slf4jVersion,
      "org.slf4j" % "log4j-over-slf4j" % Slf4jVersion % "test",
      "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
    )
  )

  lazy val root = Project("akka-debugging-tool", file("."))
    .settings(rootSettings: _*)
    .settings(
      name := "akka-debugging-tool",
      publish := {},
      publishArtifact := false
    )
    .aggregate(
      core,
      visualization,
      simpleScenarioExample
    )

  lazy val core = Project("akka-debugging-tool-core", file("core"))
    .settings(rootSettings: _*)
    .settings(
      name := "akka-debugging-tool-core",
      libraryDependencies += "org.aspectj" % "aspectjweaver" % AspectJVersion,
      libraryDependencies += "org.aspectj" % "aspectjrt" % AspectJVersion
    )

  lazy val visualization = Project("akka-debugging-tool-visualization", file("visualization"))
    .enablePlugins(PlayScala)
    .settings(rootSettings: _*)
    .settings(
      name := "akka-debugging-tool-visualization",
      libraryDependencies ++= Seq(
        jdbc,
        cache,
        ws,
        specs2 % Test,
        // Database connections
        "com.typesafe.slick" %% "slick" % SlickVersion,
        "postgresql" % "postgresql" % "9.1-901.jdbc4",
        "org.xerial" % "sqlite-jdbc" % "3.8.11.1",
        "com.zaxxer" % "HikariCP-java6" % "2.3.3"
      ),
      resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
      routesGenerator := InjectedRoutesGenerator
    )
    .dependsOn(core)

  lazy val simpleScenarioExample = Project("akka-debugging-tool-examples-simple-scenario",
    file("examples/simple-scenario"))
    .settings(rootSettings: _*)
    .settings(
      name := "akka-debugging-tool-examples-simple-scenario",
      sources in(Compile, doc) := Seq.empty,
      publishArtifact in(Compile, packageDoc) := false,
      libraryDependencies ++= Seq(
        "com.typesafe.slick" %% "slick" % SlickVersion,
        "postgresql" % "postgresql" % "9.1-901.jdbc4",
        "org.slf4j" % "slf4j-simple" % Slf4jVersion,
        "org.xerial" % "sqlite-jdbc" % "3.8.11.1",
        "com.zaxxer" % "HikariCP-java6" % "2.3.3"
      )
    )
    .dependsOn(core)
    .enablePlugins(AkkaDebuggingPlugin)

  lazy val oneToManyExample = Project("akka-debugging-tool-examples-one-to-many",
    file("examples/one-to-many"))
    .settings(rootSettings: _*)
    .settings(
      name := "akka-debugging-tool-examples-one-to-many",
      sources in(Compile, doc) := Seq.empty,
      publishArtifact in(Compile, packageDoc) := false,
      libraryDependencies ++= Seq(
        "com.typesafe.slick" %% "slick" % SlickVersion,
        "postgresql" % "postgresql" % "9.1-901.jdbc4",
        "org.slf4j" % "slf4j-simple" % Slf4jVersion,
        "org.xerial" % "sqlite-jdbc" % "3.8.11.1",
        "com.zaxxer" % "HikariCP-java6" % "2.3.3"
      )
    )
    .dependsOn(core)
    .enablePlugins(AkkaDebuggingPlugin)
}
