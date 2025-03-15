package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.pde.border.BorderCondition;
import by.andd3dfx.math.pde.solver.HyperbolicEquationSolver;

/**
 * Hyperbolic equation (described oscillation processes):
 * <p>
 * M(x,t,U)*∂²U/∂t² + L(x,t,U)*∂U/∂t = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U) where U = U(x,t)
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

    @Override
    public double gM(double x, double t, double U) {
        return 1;
    }

    /**
     * Initial condition ∂U/∂t(x,0) at the moment t=0
     *
     * @param x space coordinate
     * @return ∂U/∂t value
     */
    public double gdU_dt0(double x) {
        return 0;
    }
}
