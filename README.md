[![Build status](https://travis-ci.com/smartgov-liris/SmartGovSimulator.svg?branch=master)](https://github.com/smartgov-liris/SmartGovSimulator)

# SmartGovSimulator
A multi-agent simulator for urban mobility policies.

The framework is sufficiently generic to handle any graph-based multi-agent simulation.

# Documentation

For concrete examples and tutorials, check the [SmartGovSimulatorDocExamples repository](https://github.com/smartgov-liris/SmartGovSimulatorDocExamples).

For the complete Java API, check the online [javadoc](https://smartgov-liris.github.io/SmartGovSimulator/).

# Gradle dependency

To the `repositories` section, add : 
```
maven { url 'https://jitpack.io' }
```
And, in the `dependencies`, add : 
```
implementation 'com.github.smartgov-liris:SmartGovSimulator:master-SNAPSHOT'
```

All the SmartGov classes should now be available in your project.

# Build locally

From the repository where you want to install the source code, run :

`git clone https://github.com/smartgov-liris/SmartGovSimulator`

`cd SmartGovSimulator`

## Command line build

To build the project using the [Gradle CLI](https://docs.gradle.org/current/userguide/command_line_interface.html), run :

- `./gradlew build` (UNIX)
- `gradlew.bat build` (Windows)

This will compile the Java classes, and run all the unit tests.

## Running the test

Although the tests are already executed when building with Gradle, you might
wish to run them independently. Running all the tests is achieved with
```
./gradlew test
```
and running a subset (defined by the regexp argument) of the test can
be done with e.g.
```
./gradlew test --console=verbose --tests "org.liris.*"
```
Eventually running a single test can be done with e.g.
```
./gradlew test --tests DeliveryDriverBehaviorTest
```
One some tests are executed you will find an html based report within
the `build/reports/tests/test/index.html` subdirectory.

In order to display the names of all the tests place the following
lines within the `build.gradle` configuration file

```bash
test {
    testLogging {
        events "passed", "skipped", "failed"
    }
}
```

## Building the javadoc

Building the [Javadoc](https://en.wikipedia.org/wiki/Javadoc) is achieved 
through a gradle task

```bash
gradlew javadoc
```

that places the generated documentation within the `docs/` sub-directory and
that can be browsed with your favorite web-browser by opening the
`docs/index.html` file.

## Gradle related notes

- Gradle available tasks can be listed with

  ```bash
   ./gradlew tasks --all
   ```

   Notice the availability of the `clean` task launchable as

   ```bash
   ./gradlew clean
   ```

- The `--console=verbose` flag will have gradle display the
  task that are already up to date e.g.
  `./gradlew test --console=verbose --tests`

## IntelliJ IDEA

To import the project in the IntelliJ IDEA :

`File` -> `New` -> `Project from Existing Sources` (or `Module from Existing Sources`) -> select the `SmartGovSimulator` folder -> `Import project from external model` -> select `Gradle` -> `Finish`

## Eclipse IDE

To import the project in the Eclipse Java IDE :

`File` -> `Import...` -> `Gradle` -> `Existing Gradle Project` ->  select the `SmartGovSimulator` folder -> `Finish`

# Contacts

SmartGov is developped at the [LIRIS](https://liris.cnrs.fr/en) within the Multi-Agent System team.

For any extra information about the project, you may contact :
- Veronique Deslandres : veronique.deslandres@liris.cnrs.fr
- Paul Breugnot : paul.breugnot@univ-fcomte.fr
