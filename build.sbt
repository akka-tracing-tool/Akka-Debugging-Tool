name := "distr_stacktrace"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe" % "config" % "1.3.0",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "com.typesafe.akka" %% "akka-remote" % "2.3.9",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.aspectj" % "aspectjweaver" % "1.7.2",
  "org.aspectj" % "aspectjrt" % "1.7.2",
  "org.slf4j" % "slf4j-simple" % "1.7.7",
  //Database connections
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "com.zaxxer" % "HikariCP" % "2.3.3"
)

javaOptions += "-javaagent:" + System.getProperty("user.home") + "/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-1.7.2.jar"

fork := true
