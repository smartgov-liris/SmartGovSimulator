# SmartGovSimulator
A multi-agent simulator for urban problems.

The framework is sufficiently generic to handle any graph-based multi-agent simulation.

# Documentation

For concrete examples and tutorials, check the [wiki of this repository](https://github.com/smartgov-liris/SmartGovSimulator/wiki).

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
