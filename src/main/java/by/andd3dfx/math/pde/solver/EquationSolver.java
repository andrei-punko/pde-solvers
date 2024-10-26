package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.pde.equation.Equation;

public interface EquationSolver<E extends Equation> {

    /**
     * Solve equation using provided space & time steps
     *
     * @param eqn partial difference equation
     * @param h   space step
     * @param tau time step
     * @return equation solution
     */
    Solution<E> solve(E eqn, double h, double tau);
}
