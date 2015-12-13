Akka Tracing Tool - Examples
============================

Tracing toolkit for Akka

# Config file

Please rename config file (delete the `.example` or `template` suffix) and fill it in with your database configuration 
in following projects:

* Visualization (in folder `conf`).
* Example scenario (in folder `src/main/resources`).

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

Next, to run the example:

```
$ sbt
> project akka-debugging-tool-examples-simple-scenario
> run
```

To run the Visualization tool:

```
$ sbt
> project akka-debugging-tool-visualization
> run
```
