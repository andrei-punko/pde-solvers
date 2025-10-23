# Partial differential equations (PDE) solvers library

[![Java CI with Gradle](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml/badge.svg)](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml)
[![Branches](.github/badges/branches.svg)](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://andrei-punko.github.io/pde-solvers/)

A library for numerical solution of partial differential equations (PDE) using finite difference method and Thomas
algorithm

## Features

### Solution of parabolic equations of the form:
  ```
  L(x,t,U)*∂U/∂t = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U)
  ```
where U = U(x,t) is the unknown function (temperature, concentration, etc)

### Solution of hyperbolic equations of the form:
  ```
  M(x,t,U)*∂²U/∂t² = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U)
  ```
where U = U(x,t) is the unknown function (displacement of string, etc)

### Support for various boundary conditions:
- Dirichlet (function value at the boundary)
- Neumann (derivative value at the boundary)
- Robin (linear combination of function value and its derivative)

### Efficient numerical methods:
- Finite difference method for derivative approximation
- Thomas algorithm for solving tridiagonal systems of linear equations

## Prerequisites

- JDK 21
- Gradle (embedded in the project)

## Building the project

```bash
./gradlew clean build
```

## Generating documentation

```bash
./gradlew clean javadoc
```
Check `./build/doc/javadoc` folder  
Online documentation is available [here](https://andrei-punko.github.io/pde-solvers/)

## Supported equation types

### Parabolic equations
[ParabolicEquation class](src/main/java/io/github/andreipunko/math/pde/equation/ParabolicEquation.java)
- Describes diffusion, heat conduction, and other dissipative processes
- Characterized by the presence of only first-order time derivative
- Solution is defined on the space-time domain `[x1,x2]×[0,t2]`

### Hyperbolic equations
[HyperbolicEquation class](src/main/java/io/github/andreipunko/math/pde/equation/HyperbolicEquation.java)
- Describes wave processes and oscillations
- Characterized by the presence of second-order time derivative
- Solution is defined on the space-time domain `[x1,x2]×[0,t2]`

## Boundary conditions

### Dirichlet (definite mode)
[DirichletBorderCondition class](src/main/java/io/github/andreipunko/math/pde/border/DirichletBorderCondition.java)
- Specifies function value at the boundary: `U(x1,t) = g1(t)` or `U(x2,t) = g2(t)`

### Neumann (definite force)
[NeumannBorderCondition class](src/main/java/io/github/andreipunko/math/pde/border/NeumannBorderCondition.java)
- Specifies derivative value at the boundary: `∂U/∂x(x1,t) = g1(t)` or `∂U/∂x(x2,t) = g2(t)`

### Robin (elastic fixing)
[RobinBorderCondition class](src/main/java/io/github/andreipunko/math/pde/border/RobinBorderCondition.java)
- Specifies linear combination of function value and its derivative:
  `∂U/∂x(x1,t) = h*(U(x1,t) - Theta(t))` or
  `∂U/∂x(x2,t) = h*(U(x2,t) - Theta(t))`

## Solution methods

### Finite difference method
- Approximates derivatives using finite differences
- Transforms PDE into a system of linear equations
- Uses the Thomas algorithm to solve the resulting system

### Thomas algorithm
- Efficient algorithm for solving tridiagonal systems of linear equations
- Time complexity O(n), where n is the system size

## Library usage examples

### Parabolic equations
- [Diffusion problem with Dirichlet boundary conditions](src/test/java/io/github/andreipunko/math/pde/solver/ParabolicEquationSolverDDTest.java)
- [Diffusion problem with Neumann boundary conditions](src/test/java/io/github/andreipunko/math/pde/solver/ParabolicEquationSolverNNTest.java)
- [Heat transfer problem with mixed Dirichlet+Robin boundary conditions](src/test/java/io/github/andreipunko/math/pde/solver/ParabolicEquationSolverDRTest.java)

### Hyperbolic equations
- [Wave equation (plucked string) with Dirichlet boundary conditions](src/test/java/io/github/andreipunko/math/pde/solver/HyperbolicEquationSolverDDTest.java)

### Solution utilities
- [Solution data saving and loading](src/test/java/io/github/andreipunko/math/pde/solver/SolutionTest.java)

## How to use library in your project

### Maven

```xml
<dependency>
  <groupId>io.github.andrei-punko</groupId>
  <artifactId>pde-solvers</artifactId>
  <version>1.0.2</version>
</dependency>
```

### Gradle

```gradle
implementation 'io.github.andrei-punko:pde-solvers:1.0.2'
```

## Appendix

### Video with project description
[![YouTube link](https://markdown-videos-api.jorgenkh.no/url?url=https%3A%2F%2Fyoutu.be%2FAmPgu9vksTU)](https://youtu.be/AmPgu9vksTU)

### How to make new library release on GitHub
See [instruction](.github/how-to-make-GH-release.md)

### How to publish artifact to Maven Central
See [instruction](MAVEN_CENTRAL_PUBLISHING.md)
