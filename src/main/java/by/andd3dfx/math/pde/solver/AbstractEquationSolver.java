package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.matrix.Matrix2D;
import by.andd3dfx.math.pde.border.BorderCondition;
import by.andd3dfx.math.pde.border.BorderConditionType1;
import by.andd3dfx.math.pde.border.BorderConditionType2;
import by.andd3dfx.math.pde.border.BorderConditionType3;
import by.andd3dfx.math.pde.equation.Equation;
import by.andd3dfx.math.space.Area;
import by.andd3dfx.math.space.Interval;

/**
 * Base class of PD equation solvers. Used to avoid code duplication in child classes
 *
 * @param <E> equation class
 * @see Equation
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
    protected Area buildArea(Equation eqn, double h, double tau) {
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
    protected Matrix2D prepare(Equation eqn, Area area) {
        // Create space for equation solution
        var matrix = new Matrix2D(area.tn() + 1, area.xn() + 1);
        // Set initial value
        for (var i = 0; i <= area.xn(); i++) {
            matrix.set(0, i, eqn.gU0(area.xx(i)));
        }
        return matrix;
    }

    /**
     * Solve tridiagonal system of algebraic equations:
     * A[i]*y[i-1] - C[i]*y[i] + B[i]*y[i+1] = -F[i], 0&lt;i&lt;N
     * using tridiagonal matrix algorithm (also known as the Thomas algorithm).
     * <p>
     * Variable notations - according to "Тихонов, Самарский - Уравнения математической физики", p.590-592
     *
     * @param A         array of equation coefficients A
     * @param B         array of equation coefficients B
     * @param C         array of equation coefficients C
     * @param F         array of equation coefficients F
     * @param leftCond  params record of left border condition
     * @param rightCond params record of right border condition
     * @return solution
     */
    public static double[] solve3DiagonalEquationsSystem(double[] A, double[] B, double[] C, double[] F,
                                                         KappaNu leftCond, KappaNu rightCond) {
        int N = A.length;
        double[] Alpha = new double[N + 1];
        double[] Beta = new double[N + 1];

        // Forward phase:
        // - calculate Alpha[1], Beta[1] from left border condition
        // - calculate Alpha[i], Beta[i] for i=1,2,...,N using recurrent formula
        Alpha[1] = leftCond.kappa;
        Beta[1] = leftCond.nu;
        for (int i = 1; i < N; i++) {
            Alpha[i + 1] = B[i] / (C[i] - A[i] * Alpha[i]);
            Beta[i + 1] = (A[i] * Beta[i] + F[i]) / (C[i] - A[i] * Alpha[i]);
        }

        // Backward phase:
        // - calculate Y[N] from border condition
        // - calculate Y[i] for i=N-1,N-2,...,1,0 using recurrent formula
        var Y = new double[N + 1];
        Y[N] = (rightCond.nu + rightCond.kappa * Beta[N]) / (1 - rightCond.kappa * Alpha[N]);
        for (int i = N - 1; i >= 0; i--) {
            Y[i] = Alpha[i + 1] * Y[i + 1] + Beta[i + 1];
        }
        return Y;
    }

    /**
     * Calculate Kappa and Nu params corresponding to provided border condition.
     * These params used by tri-diagonal algorithm.
     *
     * @param borderCondition border condition
     * @param h               space step
     * @param time            time
     * @return record with Kappa and Nu values
     */
    protected KappaNu calcKappaNu(BorderCondition borderCondition, double h, double time) {
        return switch (borderCondition) {
            case BorderConditionType1 condType1 -> new KappaNu(0, condType1.gU(time));
            case BorderConditionType2 condType2 -> new KappaNu(1, -h * condType2.gdU_dx(time));
            case BorderConditionType3 condType3 -> {
                var lh = condType3.gH();
                var kappa = 1 / (1 + h * lh);
                yield new KappaNu(kappa, h * lh * kappa * condType3.gTheta(time));
            }
            default -> throw new IllegalStateException("Unexpected border condition type: " + borderCondition);
        };
    }

    /**
     * Just record to store border condition params
     *
     * @param kappa
     * @param nu
     */
    public record KappaNu(double kappa, double nu) {
    }
}
