package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.matrix.Matrix2D;
import by.andd3dfx.math.pde.border.BorderCondition;
import by.andd3dfx.math.pde.border.DirichletBorderCondition;
import by.andd3dfx.math.pde.border.NeumannBorderCondition;
import by.andd3dfx.math.pde.border.RobinBorderCondition;
import by.andd3dfx.math.pde.equation.Equation;
import by.andd3dfx.math.space.Area;
import by.andd3dfx.math.space.Interval;

/**
 * Abstract base class for partial differential equation solvers.
 * Provides common functionality and utility methods used by specific equation solvers.
 * Implements the core numerical methods and algorithms shared across different types of PDE solvers.
 *
 * @param <E> the type of equation this solver handles
 * @see Equation
 * @see EquationSolver
 */
public abstract class AbstractEquationSolver<E extends Equation> implements EquationSolver<E> {

    /**
     * Builds a space-time computational domain for the equation solution.
     * Creates a grid with specified spatial and temporal step sizes.
     *
     * @param eqn the equation to solve
     * @param h   spatial step size (must be positive)
     * @param tau temporal step size (must be positive)
     * @return the computational domain with defined grid points
     * @throws IllegalArgumentException if parameters h or tau are non-positive
     */
    protected Area buildArea(Equation eqn, double h, double tau) {
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
     * @throws IllegalArgumentException if arrays have different lengths or invalid coefficients
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
     * Calculates Kappa and Nu parameters for the tridiagonal algorithm based on boundary conditions.
     * These parameters are used to incorporate different types of boundary conditions into the solution.
     *
     * @param borderCondition the boundary condition to process
     * @param h               spatial step size
     * @param time            current time point
     * @return KappaNu record containing calculated parameters
     * @throws IllegalStateException if an unsupported boundary condition type is encountered
     */
    protected KappaNu calcKappaNu(BorderCondition borderCondition, double h, double time) {
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
