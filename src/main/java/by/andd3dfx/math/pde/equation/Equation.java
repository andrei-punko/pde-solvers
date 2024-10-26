package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.pde.border.BorderCondition;
import lombok.Getter;

/**
 * Base class. Used to avoid code duplication in child classes
 */
@Getter
public abstract class Equation {

    private final double x1;
    private final double x2;
    private final double t2;
    private final BorderCondition leftBorderCondition;
    private final BorderCondition rightBorderCondition;

    /**
     * Create equation
     *
     * @param x1                   left space coordinate
     * @param x2                   right space coordinate
     * @param t2                   right time coordinate
     * @param leftBorderCondition  left border condition
     * @param rightBorderCondition right border condition
     */
    public Equation(double x1, double x2, double t2,
                    BorderCondition leftBorderCondition,
                    BorderCondition rightBorderCondition) {
        this.x1 = x1;
        this.x2 = x2;
        this.t2 = t2;
        this.leftBorderCondition = leftBorderCondition;
        this.rightBorderCondition = rightBorderCondition;
    }

    /**
     * Initial condition U(x) at moment t=0
     *
     * @param x coordinate
     * @return U(x) value
     */
    public double gU0(double x) {
        return 0;
    }

    /**
     * Coefficient M(x,t,U) of equation for 2nd-order time derivative
     */
    public double gM(double x, double t, double U) {
        return 1;
    }

    /**
     * Coefficient K(x,t,U) of equation for 2nd-order space derivative
     */
    public double gK(double x, double t, double U) {
        return 1;
    }

    /**
     * Coefficient V(x,t,U) of equation for 1st-order space derivative
     */
    public double gV(double x, double t, double U) {
        return 0;
    }

    /**
     * Free addendum F(x,t,U) of equation
     */
    public double gF(double x, double t, double U) {
        return 0;
    }
}
