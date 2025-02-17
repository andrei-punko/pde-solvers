package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.Area;
import by.andd3dfx.math.Interval;
import by.andd3dfx.math.Matrix;
import by.andd3dfx.math.pde.border.BorderConditionType1;
import by.andd3dfx.math.pde.border.BorderConditionType2;
import by.andd3dfx.math.pde.border.BorderConditionType3;
import by.andd3dfx.math.pde.equation.Equation;
import by.andd3dfx.math.pde.equation.HyperbolicEquation;

/**
 * Base class. Used to avoid code duplication in child classes
 */
public abstract class AbstractEquationSolver<E extends Equation> implements EquationSolver<E> {

    /**
     * Build space-time area where equation will be solved
     *
     * @param eqn equation
     * @param h   space step
     * @param tau time step
     * @return built area
     */
    protected Area buildArea(HyperbolicEquation eqn, double h, double tau) {
        return new Area(
                new Interval(eqn.getX1(), eqn.getX2(), h),
                new Interval(0, eqn.getT2(), tau)
        );
    }

    /**
     * Initialization: create space for solution, set initial value
     *
     * @param eqn  equation
     * @param area space-time area on which we want to find solution
     * @return initialized matrix
     */
    protected Matrix prepare(Equation eqn, Area area) {
        // Create space for equation solution
        var matrix = new Matrix(area.tn() + 1, area.xn() + 1);
        // Set initial value
        for (var i = 0; i <= area.xn(); i++) {
            matrix.set(0, i, eqn.gU0(area.xx(i)));
        }
        return matrix;
    }

    /**
     * Tri-diagonal matrix algorithm
     */
    protected double[] progonka(Equation eqn, double h, double time, double[] A, double[] B, double[] C, double[] F) {
        double m1 = 0, m2 = 0, n1 = 0, n2 = 0;

        if (eqn.getLeftBorderCondition() instanceof BorderConditionType1 condition) {
            n1 = condition.gU(time);
        } else if (eqn.getLeftBorderCondition() instanceof BorderConditionType2 condition) {
            m1 = 1;
            n1 = -h * condition.gdU_dx(time);
        } else if (eqn.getLeftBorderCondition() instanceof BorderConditionType3 condition) {
            var lh = condition.gH();
            m1 = 1 / (1 + h * lh);
            n1 = h * lh * condition.gTheta(time) / (1 + h * lh);
        }

        if (eqn.getRightBorderCondition() instanceof BorderConditionType1 condition) {
            n2 = condition.gU(time);
        } else if (eqn.getRightBorderCondition() instanceof BorderConditionType2 condition) {
            m2 = 1;
            n2 = h * condition.gdU_dx(time);
        } else if (eqn.getRightBorderCondition() instanceof BorderConditionType3 condition) {
            var rh = condition.gH();
            m2 = 1 / (1 - h * rh);
            n2 = -h * rh * condition.gTheta(time) / (1 - h * rh);
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
