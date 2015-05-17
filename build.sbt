name := "distr_stacktrace"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "com.typesafe" % "config" % "1.3.0",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "commons-dbcp" % "commons-dbcp" % "1.4",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)