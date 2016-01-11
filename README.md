Akka Tracing Tool - Examples
============================

Tracing toolkit for Akka

# Config file

Please fill in the config files `src/main/resources/akka_tracing.conf` in each of the scenarios with correct database
configuration or use the provided ones.

# How to run

Switch to the correct project (one of `simpleScenario` or `oneToMany`) by:

```
$ cd <projectName>
```

Then launch SBT by:

```
$ sbt
```

Then run the `run` task by:

```
> run
```

# Integration test (simple scenario only)

In simple scenario there is an integration test of our library.

You can execute it by:

```
$ sbt test
```

**WARNING!** The test does not terminate the `java` process that `sbt` spawns - you need to do it yourself!

