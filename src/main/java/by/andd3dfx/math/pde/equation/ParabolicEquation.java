package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.pde.border.BorderCondition;
import by.andd3dfx.math.pde.solver.ParabolicEquationSolver;

/**
 * Parabolic equation (described heat/mass transfer):
 * <p>
 * L(x,t,U)*∂U/∂t = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U) where U = U(x,t)
 *
 * @see ParabolicEquationSolver
 */
public class ParabolicEquation extends Equation {

    /**
     * Create parabolic equation
     *
     * @param x1                   left border of space interval
     * @param x2                   right border of space interval
     * @param t2                   right border of time interval
     * @param leftBorderCondition  left border condition
     * @param rightBorderCondition right border condition
     */
    public ParabolicEquation(double x1, double x2, double t2,
                             BorderCondition leftBorderCondition,
                             BorderCondition rightBorderCondition) {
        super(x1, x2, t2, leftBorderCondition, rightBorderCondition);
    }

    @Override
    public double gL(double x, double t, double U) {
        return 1;
    }
}
