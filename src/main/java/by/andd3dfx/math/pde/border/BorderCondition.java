package by.andd3dfx.math.pde.border;

import by.andd3dfx.math.pde.equation.Equation;

/**
 * Base interface for boundary conditions in partial differential equations.
 * Defines the contract for different types of boundary conditions that can be
 * applied to the spatial boundaries of the solution domain.
 * <p>
 * Three types of boundary conditions are supported:
 * <ul>
 *   <li>Type 1: Dirichlet boundary condition (prescribed function value)</li>
 *   <li>Type 2: Neumann boundary condition (prescribed derivative value)</li>
 *   <li>Type 3: Robin boundary condition (mixed condition)</li>
 * </ul>
 *
 * @see BorderConditionType1
 * @see BorderConditionType2
 * @see BorderConditionType3
 * @see Equation
 */
public interface BorderCondition {
}
