package io.github.andreipunko.math.pde.border;

import io.github.andreipunko.math.pde.equation.Equation;

/**
 * Base interface for boundary conditions in partial differential equations.
 * Defines the contract for different types of boundary conditions that can be
 * applied to the spatial boundaries of the solution domain.
 * <p>
 * Three types of boundary conditions are supported:
 * <ul>
 *   <li>Dirichlet boundary condition (prescribed function value)</li>
 *   <li>Neumann boundary condition (prescribed derivative value)</li>
 *   <li>Robin boundary condition (mixed condition)</li>
 * </ul>
 *
 * @see DirichletBorderCondition
 * @see NeumannBorderCondition
 * @see RobinBorderCondition
 * @see Equation
 */
public interface BorderCondition {
}
