package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.Area;
import by.andd3dfx.math.Interval;
import by.andd3dfx.math.Matrix;
import by.andd3dfx.math.pde.border.BorderCondition;
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
        var mn1 = calcMN(eqn.getLeftBorderCondition(), h, time);
        var mn2 = calcMN(eqn.getRightBorderCondition(), h, time);

        int N = A.length;
        double[] Alpha = new double[N + 1];
        double[] Beta = new double[N + 1];

        Alpha[1] = mn1.m();
        Beta[1] = mn1.n();
        for (int i = 1; i < N; i++) {
            Alpha[i + 1] = B[i] / (C[i] - A[i] * Alpha[i]);
            Beta[i + 1] = (A[i] * Beta[i] + F[i]) / (C[i] - A[i] * Alpha[i]);
        }

        var Y = new double[N + 1];
        Y[N] = (mn2.n() + mn2.m() * Beta[N]) / (1 - mn2.m() * Alpha[N]);
        for (int i = N - 1; i >= 0; i--) {
            Y[i] = Alpha[i + 1] * Y[i + 1] + Beta[i + 1];
        }
        return Y;
    }

    private MN calcMN(BorderCondition borderCondition, double h, double time) {
        double m = 0, n = 0;
        if (borderCondition instanceof BorderConditionType1 condition) {
            n = condition.gU(time);
        } else if (borderCondition instanceof BorderConditionType2 condition) {
            m = 1;
            n = -h * condition.gdU_dx(time);
        } else if (borderCondition instanceof BorderConditionType3 condition) {
            var lh = condition.gH();
            m = 1 / (1 + h * lh);
            n = h * lh * condition.gTheta(time) / (1 + h * lh);
        }
        return new MN(m, n);
    }

    private record MN(double m, double n) {
    }
}
