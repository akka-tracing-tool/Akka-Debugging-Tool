logLevel := Level.Warn

addSbtPlugin("pl.edu.agh.iet" % "akka-tracing-sbt" % "0.0.2.1")

resolvers += Resolver.url("Akka Tracing", url("https://dl.bintray.com/salceson/maven/"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.1",
  "org.xerial" % "sqlite-jdbc" % "3.8.11.1"
)
