package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.pde.equation.Equation;

/**
 * Equation solver interface. All solvers should implement it
 *
 * @param <E> particular equation type
 */
public interface EquationSolver<E extends Equation> {

    /**
     * Solve equation using provided space and time steps
     *
     * @param eqn partial difference equation
     * @param h   space step
     * @param tau time step
     * @return equation solution
     */
    Solution<E> solve(E eqn, double h, double tau);
}
