[![Maintainability](https://api.codeclimate.com/v1/badges/33f6b1ec8bd617111960/maintainability)](https://codeclimate.com/github/roycetech/junit-cast/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/33f6b1ec8bd617111960/test_coverage)](https://codeclimate.com/github/roycetech/junit-cast/test_coverage)
[![CircleCI](https://circleci.com/gh/cci-mkagzi/junit-cast.svg?style=svg)](https://circleci.com/gh/cci-mkagzi/junit-cast)

# JUnit Cast

A library that leverages the JUnit parameterized tests to test all scenarios provided. 

## Overview

This project was created 10 years ago to help test a rule-heavy business logic. 

It works well when testing functions that receive an input or a set of inputs, and then an output is expected as a function return or a change in state.

It follows the AAA pattern in unit testing which I find confusing, so in this case, I called it a PEA-pattern (Prepare, Execute, and Assert). With this library, assertion is not even necessary, but I've included it for the sake of completeness.

## How to use this library

See the document: [How to write tests](./how-to-write-tests.md)

## How to use this project

This project was originally developed using Eclipse IDE should work just as 
easily using IntelliJ or any other text editors.

1. Make sure your system supports at least Java 11.
2. Build the project by running: `mvn clean package`

### IDE Requirements

This project depends on a Mockito version that was compiled on JDK 11. For this reason, it is recommended to use a JDK in your IDE that is equal or higher to version 11 so that the IDE can support all the classes used in this project.

## Known Issues

* September 8, 2023 11:01 AM - Infinitest eclipse plugin could not run the tests properly, and a stack trace is not available.
