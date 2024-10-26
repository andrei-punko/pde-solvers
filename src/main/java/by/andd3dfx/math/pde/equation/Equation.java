package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.Area;
import by.andd3dfx.math.Interval;
import by.andd3dfx.math.Matrix;
import by.andd3dfx.math.pde.border.BorderCondition;
import by.andd3dfx.math.pde.border.BorderConditionType1;
import by.andd3dfx.math.pde.border.BorderConditionType2;
import by.andd3dfx.math.pde.border.BorderConditionType3;

/**
 * Base class. Used to avoid code duplication in child classes
 */
public abstract class Equation {

    protected final Area area;
    protected final BorderCondition leftBorderCondition;
    protected final BorderCondition rightBorderCondition;

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
        this.area = new Area(new Interval(x1, x2, 1), new Interval(0, t2, 1));
        this.leftBorderCondition = leftBorderCondition;
        this.rightBorderCondition = rightBorderCondition;
    }

    /**
     * Initial condition U(x) at moment t=0
     *
     * @param x coordinate
     * @return U(x) value
     */
    protected double gU0(double x) {
        return 0;
    }

    /**
     * Coefficient M(x,t,U) of equation for 2nd-order time derivative
     */
    protected double gM(double x, double t, double U) {
        return 1;
    }

    /**
     * Coefficient K(x,t,U) of equation for 2nd-order space derivative
     */
    protected double gK(double x, double t, double U) {
        return 1;
    }

    /**
     * Coefficient V(x,t,U) of equation for 1st-order space derivative
     */
    protected double gV(double x, double t, double U) {
        return 0;
    }

    /**
     * Free addendum F(x,t,U) of equation
     */
    protected double gF(double x, double t, double U) {
        return 0;
    }

    /**
     * Solve equation using provided space & time steps
     *
     * @param h   space step
     * @param tau time step
     */
    public abstract Solution solve(double h, double tau);

    /**
     * Initialization: create space for solution, set initial value
     *
     * @param h   space step
     * @param tau time step
     * @return initialized matrix
     */
    protected Matrix prepare(double h, double tau) {
        assert (h > 0 && tau > 0);
        area.x().reborn(h);
        area.t().reborn(tau);

        // Create space for equation solution
        var matrix = new Matrix(area.t().n() + 1, area.x().n() + 1);
        // Set initial value
        for (var i = 0; i <= area.x().n(); i++) {
            matrix.set(0, i, gU0(area.x().x(i)));
        }
        return matrix;
    }

    /**
     * Tridiagonal matrix algorithm
     */
    protected double[] progonka(double h, int nj, double[] A, double[] B, double[] C, double[] F) {
        double m1 = 0, m2 = 0, n1 = 0, n2 = 0;
        double t = area.t().x(nj);

        if (leftBorderCondition instanceof BorderConditionType1 condition) {
            n1 = condition.gU(t);
        } else if (leftBorderCondition instanceof BorderConditionType2 condition) {
            m1 = 1;
            n1 = -h * condition.gdU_dx(t);
        } else if (leftBorderCondition instanceof BorderConditionType3 condition) {
            var lh = condition.gH();
            m1 = 1 / (1 + h * lh);
            n1 = h * lh * condition.gTheta(t) / (1 + h * lh);
        }

        if (rightBorderCondition instanceof BorderConditionType1 condition) {
            n2 = condition.gU(t);
        } else if (rightBorderCondition instanceof BorderConditionType2 condition) {
            m2 = 1;
            n2 = h * condition.gdU_dx(t);
        } else if (rightBorderCondition instanceof BorderConditionType3 condition) {
            var rh = condition.gH();
            m2 = 1 / (1 - h * rh);
            n2 = -h * rh * condition.gTheta(t) / (1 - h * rh);
        }

        int N = A.length;
        double[] Alpha = new double[N + 1];
        double[] Beta = new double[N + 1];

        Alpha[1] = m1;
        Beta[1] = n1;
        for (int i = 1; i < N; i++) {
            Alpha[i + 1] = B[i] / (C[i] - A[i] * Alpha[i]);
            Beta[i + 1] = (A[i] * Beta[i] + F[i]) / (C[i] - A[i] * Alpha[i]);
        }

        var Y = new double[N + 1];
        Y[N] = (n2 + m2 * Beta[N]) / (1 - m2 * Alpha[N]);
        for (int i = N - 1; i >= 0; i--) {
            Y[i] = Alpha[i + 1] * Y[i + 1] + Beta[i + 1];
        }
        return Y;
    }
}
