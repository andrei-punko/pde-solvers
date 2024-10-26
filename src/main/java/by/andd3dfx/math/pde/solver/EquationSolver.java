package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.pde.equation.Equation;
import by.andd3dfx.math.pde.equation.Solution;

public interface EquationSolver<E extends Equation> {

    /**
     * Solve equation
     *
     * @param eqn partial difference equation
     * @param h   space step
     * @param tau time step
     * @return equation solution
     */
    Solution<E> solve(E eqn, double h, double tau);
}
