Akka-Debugging-Tool
===================

Debugging toolkit for Akka

# Config file

Please rename config file (delete the `.example` suffix) and fill it in with your database configuration.

# How to run (at this moment)

First, you need to publish (locally) the core part:

```
$ sbt
> project akka-debugging-tool-core
> publishLocal
```

Next, you need to compile the whole project:

```
$ sbt compile
```

Next, to run the project
