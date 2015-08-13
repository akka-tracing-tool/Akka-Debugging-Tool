name := "actor-sample-scenario"

version := "1.0"

scalaVersion := "2.11.6"

lazy val actors = SettingKey[Seq[String]]("Actors.")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "org.mariusz89016" % "distr_stacktrace_2.11" % "1.0_SNAPSHOT" % Runtime,
  "org.mariusz89016" % "distr_stacktrace_2.11" % "1.0_SNAPSHOT"
)

javaOptions += "-javaagent:" + System.getProperty("user.home") + "/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-1.7.2.jar"

fork := true

actors := Seq("com.example.actors.FirstActor", "com.example.actors.SecondActor", "com.example.actors.ThirdActor")

sourceGenerators in Compile <+= (baseDirectory,  scalaSource in Compile, actors) map { (baseDir, dir, actors) =>
  val file = dir / "akka" / "MethodBang.scala"
//  val template = dir / "akka" / "MethodBangTemplate"

  val methodBangFile = IO.toFile(new URL("file://"+Path.userHome.absolutePath+"/.ivy2/local/org.mariusz89016/distr_stacktrace_2.11/1.0_SNAPSHOT/srcs/distr_stacktrace_2.11-sources.jar"))

  IO.unzip(methodBangFile, baseDir / "unpack")
  val template = baseDir / "unpack" / "akka" / "MethodBang_template.scala"

  val within = generateWithin(actors)
  val output = IO.readLines(template) map { line =>
    line.contains("<<<ACTORS>>>") match {
      case true =>
        line.replace("<<<ACTORS>>>", within)
      case _ =>
        line
    }
  }

  IO.write(file, output.mkString("\n"))
  Seq(file)
}

def generateWithin(actors: Seq[String]): String = {
  actors.map { actor =>
    s"within(${actor.toString()})"
  }.mkString("(", " || ", ")")
}