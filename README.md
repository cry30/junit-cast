[![Maintainability](https://api.codeclimate.com/v1/badges/33f6b1ec8bd617111960/maintainability)](https://codeclimate.com/github/roycetech/junit-cast/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/33f6b1ec8bd617111960/test_coverage)](https://codeclimate.com/github/roycetech/junit-cast/test_coverage)
[![CircleCI](https://dl.circleci.com/status-badge/img/circleci/8icWCYAEYCg5qEpZBqxQK1/4oRakASGeyEGFn55CaDJdK/tree/master.svg?style=svg&circle-token=cab96ffce5a3b5ee4869faa6a7f0926413465b2e)](https://dl.circleci.com/status-badge/redirect/circleci/8icWCYAEYCg5qEpZBqxQK1/4oRakASGeyEGFn55CaDJdK/tree/master)
![Maven Central](https://img.shields.io/maven-central/v/io.github.roycetech/junit-cast?link=https%3A%2F%2Fcentral.sonatype.com%2Fartifact%2Fio.github.roycetech%2Fjunit-cast)

# JUnit Cast

A library that leverages the JUnit parameterized tests to test all scenarios provided. 

## Overview

This project was created 10 years ago to help test a rule-heavy business logic. 

It works well when testing functions that receive an input or a set of inputs, and then an output is expected as a return value or a change in state.

It follows the AAA pattern in unit testing, which I find confusing, so in this case, I called it a PEA-pattern (Prepare, Execute, and Assert). With this library, assertion is implicit.

## How to write a test

![](./assets/images/demo-short.gif)
[How to write tests - Text Version](./how-to-write-tests.md)

## How to use this project

1. Make sure your system supports at least Java 11.
2. Build the project by running: `mvn clean package`

### IDE Requirements

This project depends on a Mockito version that was compiled on JDK 11. For this reason, it is recommended to use a JDK in your IDE that is equal or higher to version 11 so that the IDE can support all the classes used in this project.
