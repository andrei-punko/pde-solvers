# Solvers of PDE (partial differential equations)

[![Java CI with Gradle](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml/badge.svg)](https://github.com/andrei-punko/pde-solvers/actions/workflows/gradle.yml)

Migration to Java some part of C/C++ code from my [math-library](https://github.com/andrei-punko/math-library) repository

## Prerequisites
- JDK 21

Gradle isn't required because of embedded Gradle presence in the project

## How to build
```shell
./gradlew clean build
```

## Supported PD equation types
- Parabolic equation (described heat/mass transfer process):  
  M(x,t,U)*dU_dt = dU( K(x,t,U)*dU_dx )_dx + V(x,t,U)*dU_dx + F(x,t,U) where U = U(x,t)  
  Check [ParabolicEquation](src/main/java/by/andd3dfx/math/pde/equation/ParabolicEquation.java) for details

- Hyperbolic equation (described oscillation process):  
  M(x,t,U)*d2U_dt2 + L(x,t,U)*dU_dt = dU( K(x,t,U)*dU_dx )_dx + V(x,t,U)*dU_dx + F(x,t,U) where U = U(x,t)  
  Check [HyperbolicEquation](src/main/java/by/andd3dfx/math/pde/equation/HyperbolicEquation.java) for details

## Supported border conditions types
- Border condition type 1 (definite mode): U(X,t)
- Border condition type 2 (definite force): dU_dx(X,t)
- Border condition type 3 (elastic fixing): dU_dx(X,t) = h*(U(X,t) - Theta(t))

## Solver
The finite-difference method was used, in result we get tridiagonal system of linear equations 
and solve it with help of tridiagonal matrix algorithm (or Thomas algorithm) (or "метод прогонки" in Russian).

## Usage notes
Check solved problems in tests:
- [Diffusion problem with type 1 of both border conditions](src/test/java/by/andd3dfx/math/pde/solver/ParabolicEquationSolver11Test.java)
- [Diffusion problem with type 2 of both border conditions](src/test/java/by/andd3dfx/math/pde/solver/ParabolicEquationSolver22Test.java)
- [Wave equation (plucked string) with type 1 of both border conditions](src/test/java/by/andd3dfx/math/pde/solver/HyperbolicEquationSolver11Test.java)
