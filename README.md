Akka Tracing Tool - Examples
============================

Tracing toolkit for Akka

# Config file

Please fill in the config files `src/main/resources/akka_tracing.conf` in each of the scenarios with correct database
configuration or use the provided ones.

# How to run

Launch sbt:

```
$ sbt
```

Then switch to the correct project (one of `simpleScenario` or `oneToMany`) by:

```
> project <projectName>
```

Then run the `run` task by:

```
> run
```
