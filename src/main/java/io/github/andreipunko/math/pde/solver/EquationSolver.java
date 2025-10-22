package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.pde.equation.Equation;

/**
 * Interface for numerical solvers of partial differential equations.
 * Defines the contract that all PDE solvers must implement to provide
 * numerical solutions for different types of partial differential equations.
 *
 * @param <E> the type of partial differential equation this solver handles
 * @see Equation
 * @see Solution
 */
public interface EquationSolver<E extends Equation> {

    /**
     * Solves a partial differential equation using numerical methods.
     * The solution is found on a space-time grid defined by the spatial and temporal step sizes.
     *
     * @param eqn the partial differential equation to solve
     * @param h   spatial step size (must be positive)
     * @param tau temporal step size (must be positive)
     * @return Solution containing the numerical solution on the space-time grid
     * @throws IllegalArgumentException if parameters h or tau are non-positive
     */
    Solution<E> solve(E eqn, double h, double tau);
}
