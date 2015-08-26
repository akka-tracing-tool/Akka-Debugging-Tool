lazy val actors = SettingKey[Seq[String]]("Actors.")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "pl.edu.agh.iet" % "akka-debugging-tool-core_2.11" % "0.0.1-SNAPSHOT" % Runtime,
  "pl.edu.agh.iet" % "akka-debugging-tool-core_2.11" % "0.0.1-SNAPSHOT"
)

javaOptions += "-javaagent:" + System.getProperty("user.home") + "/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-1.7.2.jar"

fork := true

actors := Seq(
  "pl.edu.agh.iet.akka_debugging.examples.actors.FirstActor",
  "pl.edu.agh.iet.akka_debugging.examples.actors.SecondActor"
)

sourceGenerators in Compile <+= (baseDirectory, scalaSource in Compile, actors) map { (baseDir, dir, actors) =>
  val file = dir / "akka" / "MethodBangAspect.scala"
  //  val template = dir / "akka" / "MethodBangTemplate"

  val methodBangFile = IO.toFile(new URL("file://" + Path.userHome.absolutePath
    + "/.ivy2/local/pl.edu.agh.iet/akka-debugging-tool-core_2.11/0.0.1-SNAPSHOT/srcs/"
    + "akka-debugging-tool-core_2.11-sources.jar"))

  IO.unzip(methodBangFile, baseDir / "unpack")
  val template = baseDir / "unpack" / "akka" / "MethodBang_template.scala"

  val within = generateWithin(actors)
  val output = IO.readLines(template) map { line =>
    line.contains("<<<ACTORS>>>") match {
      case true =>
        line.replace("<<<ACTORS>>>", within)
      case _ =>
        line.contains("class MethodBang") match {
          case true =>
            line.replace("class MethodBang", "class MethodBangAspect")
          case _ =>
            line.contains("//@Aspect") match {
              case true =>
                line.replace("//@Aspect", "@Aspect")
              case _ =>
                line
            }
        }
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
