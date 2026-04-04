package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.matrix.Matrix2D;
import io.github.andreipunko.math.pde.border.BorderCondition;
import io.github.andreipunko.math.pde.border.DirichletBorderCondition;
import io.github.andreipunko.math.pde.border.NeumannBorderCondition;
import io.github.andreipunko.math.pde.border.RobinBorderCondition;
import io.github.andreipunko.math.pde.equation.Equation;
import io.github.andreipunko.math.space.Area;
import io.github.andreipunko.math.space.Interval;

/**
 * Abstract base class for partial differential equation solvers.
 * Provides common functionality and utility methods used by specific equation solvers.
 * Implements the core numerical methods and algorithms shared across different types of PDE solvers.
 * <p>
 * Step sizes passed to {@link #buildArea} are validated for finiteness and positivity only; physical or discrete
 * stability of the chosen {@code h} and {@code tau} is not enforced (see package {@code io.github.andreipunko.math.pde.solver}).
 *
 * @param <E> the type of equation this solver handles
 * @see Equation
 * @see EquationSolver
 */
public abstract class AbstractEquationSolver<E extends Equation> implements EquationSolver<E> {

    /**
     * Constructor for use by concrete solver subclasses.
     */
    protected AbstractEquationSolver() {
    }

    /**
     * Builds a space-time computational domain for the equation solution.
     * Creates a grid with specified spatial and temporal step sizes.
     *
     * @param eqn the equation to solve
     * @param h   spatial step size (must be positive)
     * @param tau temporal step size (must be positive)
     * @return the computational domain with defined grid points
     * @throws IllegalArgumentException if eqn is null, or if h or tau are not finite or not positive
     */
    protected Area buildArea(Equation eqn, double h, double tau) {
        if (eqn == null) {
            throw new IllegalArgumentException("eqn must not be null");
        }
        if (!Double.isFinite(h) || h <= 0) {
            throw new IllegalArgumentException("spatial step h must be finite and positive, got: " + h);
        }
        if (!Double.isFinite(tau) || tau <= 0) {
            throw new IllegalArgumentException("time step tau must be finite and positive, got: " + tau);
        }
        return new Area(
                new Interval(eqn.getX1(), eqn.getX2(), h),
                new Interval(0, eqn.getT2(), tau)
        );
    }

    /**
     * Initializes the solution matrix with initial conditions.
     * Creates a matrix to store the solution and sets the initial values
     * based on the equation's initial condition.
     *
     * @param eqn  the equation to solve
     * @param area the computational domain where solution will be found
     * @return initialized matrix with initial conditions
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
     * Solves a tridiagonal system of linear algebraic equations using the Thomas algorithm.
     * The system has the form: A[i]*y[i-1] - C[i]*y[i] + B[i]*y[i+1] = -F[i], 0&lt;i&lt;N
     * <p>
     * The algorithm consists of two phases:
     * <ol>
     *   <li>Forward phase: computes coefficients Alpha[i] and Beta[i]</li>
     *   <li>Backward phase: computes the solution Y[i]</li>
     * </ol>
     * <p>
     * Variable notations follow "Tikhonov, Samarskii - Equations of Mathematical Physics", p.590-592
     *
     * @param A         coefficients for y[i-1] terms
     * @param B         coefficients for y[i+1] terms
     * @param C         coefficients for y[i] terms
     * @param F         right-hand side terms
     * @param leftCond  left boundary condition parameters
     * @param rightCond right boundary condition parameters
     * @return solution vector Y[i]
     * @throws IllegalArgumentException if arrays have different lengths, if coefficients are invalid, or if a forward /
     *                                    right-boundary denominator is zero or numerically too small (singular or
     *                                    ill-conditioned sweep)
     */
    public static double[] solve3DiagonalEquationsSystem(double[] A, double[] B, double[] C, double[] F,
                                                         KappaNu leftCond, KappaNu rightCond) {
        if (A == null || B == null || C == null || F == null) {
            throw new IllegalArgumentException("coefficient arrays A, B, C, F must not be null");
        }
        if (leftCond == null || rightCond == null) {
            throw new IllegalArgumentException("boundary parameters leftCond and rightCond must not be null");
        }
        int nA = A.length;
        if (nA != B.length || nA != C.length || nA != F.length) {
            throw new IllegalArgumentException(
                    "A, B, C, F must have the same length; got " + nA + ", " + B.length + ", " + C.length + ", " + F.length);
        }
        if (nA < 1) {
            throw new IllegalArgumentException("system size must be at least 1, got: " + nA);
        }
        int N = nA;
        double[] Alpha = new double[N + 1];
        double[] Beta = new double[N + 1];

        // Forward phase:
        // - calculate Alpha[1], Beta[1] from left border condition
        // - calculate Alpha[i], Beta[i] for i=1,2,...,N using recurrent formula
        Alpha[1] = leftCond.kappa;
        Beta[1] = leftCond.nu;
        for (int i = 1; i < N; i++) {
            double denom = C[i] - A[i] * Alpha[i];
            Alpha[i + 1] = divideThomas(B[i], denom, "forward sweep (Alpha), row index " + i);
            Beta[i + 1] = divideThomas(A[i] * Beta[i] + F[i], denom, "forward sweep (Beta), row index " + i);
        }

        // Backward phase:
        // - calculate Y[N] from border condition
        // - calculate Y[i] for i=N-1,N-2,...,1,0 using recurrent formula
        var Y = new double[N + 1];
        double denomRight = 1 - rightCond.kappa * Alpha[N];
        Y[N] = divideThomas(rightCond.nu + rightCond.kappa * Beta[N], denomRight, "right boundary (Y[N])");
        for (int i = N - 1; i >= 0; i--) {
            Y[i] = Alpha[i + 1] * Y[i + 1] + Beta[i + 1];
        }
        return Y;
    }

    /**
     * Divides {@code numerator} by {@code denominator} for the Thomas algorithm, rejecting non-finite values and
     * near-zero denominators relative to the magnitude of the operands.
     */
    private static double divideThomas(double numerator, double denominator, String stage) {
        if (!Double.isFinite(denominator)) {
            throw new IllegalArgumentException("Tridiagonal solver (" + stage + "): denominator is not finite: " + denominator);
        }
        double scale = Math.max(1.0, Math.abs(numerator) + Math.abs(denominator));
        final double relTol = 1e-14;
        if (Math.abs(denominator) <= relTol * scale) {
            throw new IllegalArgumentException(
                    "Tridiagonal solver (" + stage + "): denominator too small in magnitude: " + denominator);
        }
        double q = numerator / denominator;
        if (!Double.isFinite(q)) {
            throw new IllegalArgumentException("Tridiagonal solver (" + stage + "): quotient is not finite");
        }
        return q;
    }

    /**
     * Calculates Kappa and Nu parameters for the tridiagonal algorithm based on boundary conditions.
     * These parameters are used to incorporate different types of boundary conditions into the solution.
     *
     * @param borderCondition the boundary condition to process
     * @param h               spatial step size
     * @param time            current time point
     * @return KappaNu record containing calculated parameters
     * @throws IllegalArgumentException if borderCondition is null
     * @throws IllegalStateException if an unsupported boundary condition type is encountered
     */
    protected KappaNu calcKappaNu(BorderCondition borderCondition, double h, double time) {
        if (borderCondition == null) {
            throw new IllegalArgumentException("borderCondition must not be null");
        }
        return switch (borderCondition) {
            case DirichletBorderCondition condition -> new KappaNu(0, condition.gU(time));

            case NeumannBorderCondition condition -> new KappaNu(1, -h * condition.gdU_dx(time));

            case RobinBorderCondition condition -> {
                var lh = condition.gH();
                var kappa = 1 / (1 + h * lh);
                yield new KappaNu(kappa, h * lh * kappa * condition.gTheta(time));
            }

            default -> throw new IllegalStateException("Unexpected border condition type: " + borderCondition);
        };
    }

    /**
     * Record class to store boundary condition parameters for the tridiagonal algorithm.
     *
     * @param kappa coefficient for the boundary condition
     * @param nu    right-hand side term for the boundary condition
     */
    public record KappaNu(double kappa, double nu) {
    }
}
