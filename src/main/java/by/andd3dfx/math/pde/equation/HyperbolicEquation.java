package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.pde.border.BorderCondition;
import by.andd3dfx.math.pde.solver.HyperbolicEquationSolver;

/**
 * Hyperbolic equation (described oscillation processes):
 * <p>
 * M(x,t,U)*d2U_dt2 + L(x,t,U)*dU_dt = dU( K(x,t,U)*dU_dx )_dx + V(x,t,U)*dU_dx + F(x,t,U) where U = U(x,t)
 *
 * @see HyperbolicEquationSolver
 */
public class HyperbolicEquation extends Equation {

    /**
     * Create hyperbolic equation
     *
     * @param x1                   left border of space interval
     * @param x2                   right border of space interval
     * @param t2                   right border of time interval
     * @param leftBorderCondition  left border condition
     * @param rightBorderCondition right border condition
     */
    public HyperbolicEquation(double x1, double x2, double t2,
                              BorderCondition leftBorderCondition,
                              BorderCondition rightBorderCondition) {
        super(x1, x2, t2, leftBorderCondition, rightBorderCondition);
    }

    /**
     * Initial condition dU_dt(x,0) at the moment t=0
     *
     * @param x space coordinate
     * @return dU_dt value
     */
    public double gdU_dt0(double x) {
        return 0;
    }

    /**
     * Coefficient L(x,t,U) of equation for 1st-order time derivative
     *
     * @param x space coordinate
     * @param t time
     * @param U U value
     * @return L value
     */
    public double gL(double x, double t, double U) {
        return 0;
    }
}
