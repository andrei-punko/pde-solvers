# Solvers of PDE (partial differential equations)

[![Java CI with Gradle](https://github.com/andrei-punko/math-pde/actions/workflows/gradle.yml/badge.svg)](https://github.com/andrei-punko/math-pde/actions/workflows/gradle.yml)

Migration to Java some part of C/C++ code from my [math-library](https://github.com/andrei-punko/math-library) repository

## Prerequisites

- JDK 21

Gradle isn't required because of embedded Gradle presence in the project

## Supported PD equation types

- Parabolic equation (described heat/mass transfer)  
  Check [ParabolicEquation](src/main/java/by/andd3dfx/math/pde/ParabolicEquation.java) class for details

- Hyperbolic equation (described oscillation processes)  
  Check [HyperbolicEquation](src/main/java/by/andd3dfx/math/pde/HyperbolicEquation.java) class for details

## Solver
The finite-difference method was used, in result we get tridiagonal system of linear equations 
and solve it with help of tridiagonal matrix algorithm (or Thomas algorithm).

In Russian - algorithm has name "Progonka" (метод прогонки).

## How to build

```shell
./gradlew clean build
```

## Usage notes

Check classes javadocs
