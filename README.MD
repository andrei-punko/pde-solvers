# Partial Differential Equations (PDE) Solvers

[![Java CI with Gradle](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml/badge.svg)](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml)
[![Branches](.github/badges/branches.svg)](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://andrei-punko.github.io/pde-solvers/)

A library for numerical solution of partial differential equations (PDE). 
This is a port from C++ code from my [math-library](https://github.com/andrei-punko/math-library) repository.

## Features

- Solution of parabolic equations of the form:
  ```
  L(x,t,U)*∂U/∂t = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U)
  ```
  where U = U(x,t) is the unknown function (temperature, concentration, etc.)

- Solution of hyperbolic equations of the form:
  ```
  M(x,t,U)*∂²U/∂t² = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U)
  ```
  where U = U(x,t) is the unknown function (displacement of string, etc.)

- Support for various boundary conditions:
  - Dirichlet (function value at the boundary)
  - Neumann (derivative value at the boundary)
  - Robin (linear combination of function value and its derivative)

- Efficient numerical methods:
  - Finite difference method for derivative approximation
  - Thomas algorithm for solving tridiagonal systems of linear equations

## Prerequisites

- JDK 21
- Gradle (embedded in the project)

## Building the Project

```bash
./gradlew clean build
```

## Generating Documentation

```bash
./gradlew clean javadoc
```
Check `build/doc/javadoc` folder  
Online documentation is available [here](https://andrei-punko.github.io/pde-solvers/)

## Supported Equation Types

### Parabolic Equations
[ParabolicEquation.java](src/main/java/by/andd3dfx/math/pde/equation/ParabolicEquation.java)
- Describes diffusion, heat conduction, and other dissipative processes
- Characterized by the presence of only first-order time derivative
- Solution is defined on the space-time domain [x1,x2]×[0,t2]

### Hyperbolic Equations
[HyperbolicEquation.java](src/main/java/by/andd3dfx/math/pde/equation/HyperbolicEquation.java)
- Describes wave processes and oscillations
- Characterized by the presence of second-order time derivative
- Solution is defined on the space-time domain [x1,x2]×[0,t2]

## Boundary Conditions

### Dirichlet (definite mode)
[BorderCondition1.java](src/main/java/by/andd3dfx/math/pde/border/BorderConditionType1.java)
- Specifies function value at the boundary: U(x1,t) = g1(t) or U(x2,t) = g2(t)

### Neumann (definite force)
[BorderCondition2.java](src/main/java/by/andd3dfx/math/pde/border/BorderConditionType2.java)
- Specifies derivative value at the boundary: ∂U/∂x(x1,t) = g1(t) or ∂U/∂x(x2,t) = g2(t)

### Robin (elastic fixing)
[BorderCondition3.java](src/main/java/by/andd3dfx/math/pde/border/BorderConditionType3.java)
- Specifies linear combination of function value and its derivative:
  ∂U/∂x(x1,t) = h*(U(x1,t) - Theta(t)) or
  ∂U/∂x(x2,t) = h*(U(x2,t) - Theta(t))

## Solution Methods

### Finite Difference Method
- Approximates derivatives using finite differences
- Transforms PDE into a system of linear equations
- Uses the Thomas algorithm to solve the resulting system

### Thomas Algorithm
- Efficient algorithm for solving tridiagonal systems of linear equations
- Time complexity O(n), where n is the system size

## Usage Examples

### Parabolic Equations
- [Diffusion problem with Dirichlet boundary conditions](src/test/java/by/andd3dfx/math/pde/solver/ParabolicEquationSolver11Test.java)
- [Diffusion problem with Neumann boundary conditions](src/test/java/by/andd3dfx/math/pde/solver/ParabolicEquationSolver22Test.java)
- [Heat transfer problem with mixed boundary conditions](src/test/java/by/andd3dfx/math/pde/solver/ParabolicEquationSolver13Test.java)

### Hyperbolic Equations
- [Wave equation (plucked string) with Dirichlet boundary conditions](src/test/java/by/andd3dfx/math/pde/solver/HyperbolicEquationSolver11Test.java)

### Solution Utilities
- [Solution data saving and loading](src/test/java/by/andd3dfx/math/pde/solver/SolutionTest.java)

## License

MIT License
