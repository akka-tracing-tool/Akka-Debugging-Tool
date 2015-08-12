name := "distr_stacktrace"

organization := "org.mariusz89016"

version := "1.0_SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  // Akka
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.akka" %% "akka-remote" % "2.3.9",
  // Config loader
  "com.typesafe" % "config" % "1.3.0",
  // Tests
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  // AspectJ
  "org.aspectj" % "aspectjweaver" % "1.7.2",
  "org.aspectj" % "aspectjrt" % "1.7.2",
  // Logger
  "org.slf4j" % "slf4j-simple" % "1.7.7",
  // Database connections
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "postgresql" % "postgresql" % "9.1-901.jdbc4"
)

javaOptions += "-javaagent:" + System.getProperty("user.home") + "/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-1.7.2.jar"

fork := true
