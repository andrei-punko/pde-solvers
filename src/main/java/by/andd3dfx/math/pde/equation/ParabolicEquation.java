package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.pde.border.BorderCondition;

/**
 * Parabolic equation (described heat/mass transfer):
 * <p>
 * M(x,t,U)*dU_dt = dU( K(x,t,U)*dU_dx )_dx + V(x,t,U)*dU_dx + F(x,t,U) where U = U(x,t)
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
}
