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
     * <p>
     * Choosing {@code h} and {@code tau} is the caller's responsibility: the library does not check CFL-type or
     * other problem-specific stability bounds (see the package summary for {@code io.github.andreipunko.math.pde.solver}).
     *
     * @param eqn the partial differential equation to solve (must not be null)
     * @param h   spatial step size (must be finite and positive)
     * @param tau temporal step size (must be finite and positive)
     * @return Solution containing the numerical solution on the space-time grid
     * @throws IllegalArgumentException if eqn is null, or if h or tau are not finite or not positive, or if an
     *                                  intermediate tridiagonal system is singular or numerically degenerate
     */
    Solution<E> solve(E eqn, double h, double tau);
}
