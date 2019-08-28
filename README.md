# SmartGovSimulator
A multi-agent simulator for urban problems.

The framework is sufficiently generic to handle any graph-based multi-agent simulation.

For concrete examples and tutorials, check the [wiki](https://github.com/smartgov-liris/SmartGovSimulator/wiki).

For the complete java API, check the online [javadoc](https://smartgov-liris.github.io/SmartGovSimulator/).

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

- `./gradlew build` (Linux)

This will compile the Java classes, and run all the unit tests.

## IntelliJ IDEA

To import the project in the IntelliJ IDEA :
`File` -> `New` -> `Project from Existing Sources` -> select the `SmartGovSimulator` folder -> `Import project from external model` -> select `Gradle` -> `Finish`

