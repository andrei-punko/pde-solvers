# Solvers of PDE (partial differential equations)

[![Java CI with Gradle](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml/badge.svg)](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml)
[![Branches](.github/badges/branches.svg)](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://andrei-punko.github.io/pde-solvers/)

Migration to Java some part of C/C++ code from my [math-library](https://github.com/andrei-punko/math-library)
repository

## Prerequisites

- JDK 21

Gradle isn't required because of embedded Gradle presence in the project

## How to build

```shell
./gradlew clean build
```

## How to generate javadocs

```shell
./gradlew javadoc
```
Check `build/doc/javadoc` folder  
In the web javadoc available [here](https://andrei-punko.github.io/pde-solvers/)

## Supported PD equation types

- Parabolic equation (described for example heat/mass transfer process):  
  `L(x,t,U)*∂U/∂t = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U) where U = U(x,t)`  
  Check [ParabolicEquation](src/main/java/by/andd3dfx/math/pde/equation/ParabolicEquation.java) for details

- Hyperbolic equation (described for example oscillation process):  
  `M(x,t,U)*∂²U/∂t² + L(x,t,U)*∂U/∂t = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U) where U = U(x,t)`  
  Check [HyperbolicEquation](src/main/java/by/andd3dfx/math/pde/equation/HyperbolicEquation.java) for details

## Supported border conditions types

- Border condition type 1 (definite mode): `U(X,t)`
- Border condition type 2 (definite force): `∂U/∂x(X,t)`
- Border condition type 3 (elastic fixing): `∂U/∂x(X,t) = h*(U(X,t) - Theta(t))`

## Solver

The finite-difference method was used, in result we get tridiagonal system of linear equations
and solve it with help of tridiagonal matrix algorithm (or Thomas algorithm).

## Usage notes

Check solved problems in tests:

- [Diffusion problem with type 1 of both border conditions](src/test/java/by/andd3dfx/math/pde/solver/ParabolicEquationSolver11Test.java)
- [Diffusion problem with type 2 of both border conditions](src/test/java/by/andd3dfx/math/pde/solver/ParabolicEquationSolver22Test.java)
- [Heat transfer problem with border conditions of type 1 and 3](src/test/java/by/andd3dfx/math/pde/solver/ParabolicEquationSolver13Test.java)
- [Wave equation (plucked string) with type 1 of both border conditions](src/test/java/by/andd3dfx/math/pde/solver/HyperbolicEquationSolver11Test.java)
