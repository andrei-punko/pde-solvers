package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.Area;
import by.andd3dfx.math.Interval;
import by.andd3dfx.math.Matrix;
import by.andd3dfx.math.pde.border.BorderCondition;
import by.andd3dfx.util.FileUtil;

/**
 * Base class. Used to avoid code duplication in child classes
 */
public abstract class Equation {

    protected final Area area;
    protected final BorderCondition leftBorderCondition;
    protected final BorderCondition rightBorderCondition;
    protected Matrix solution;

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
    public abstract void solve(double h, double tau);

    /**
     * Initialization of equation: create space for solution, set initial value
     *
     * @param h   space step
     * @param tau time step
     */
    protected void prepare(double h, double tau) {
        assert (h > 0 && tau > 0);
        area.x().reborn(h);
        area.t().reborn(tau);

        // Create space for equation solution
        solution = new Matrix(area.t().n() + 1, area.x().n() + 1);
        // Set initial value
        for (var i = 0; i <= area.x().n(); i++) {
            solution.set(0, i, gU0(area.x().x(i)));
        }
    }

    /**
     * Save data U(x) for asked time moment
     *
     * @param fileName file name
     * @param t        time
     */
    public void sUt(String fileName, double t) {
        sUt(fileName, new double[]{t});
    }

    /**
     * Save data U(x,t_i) for asked time moments [t_i].
     * So in result we get some set of slices for several time moments
     *
     * @param fileName file name
     * @param t        times array
     */
    public void sUt(String fileName, double[] t) {
        for (var t_i : t) {
            assert (area.tLeft() <= t_i && t_i <= area.tRight());
        }

        var sb = new StringBuilder();
        for (var i = 0; i <= area.x().n(); i++) {
            sb.append(area.x().x(i));
            for (var t_i : t) {
                sb.append(" ").append(solution.get(area.t().i(t_i), i));
            }
            sb.append("\n");
        }
        FileUtil.serialize(fileName, sb);
    }

    /**
     * Save data U(t) for definite space coordinate
     *
     * @param fileName file name
     * @param x        space coordinate
     */
    public void sUx(String fileName, double x) {
        sUx(fileName, new double[]{x});
    }

    /**
     * Save data U(x_i, t) for asked space coordinates [x_i].
     * So in result we get some set of slices for several space coordinates
     *
     * @param fileName file name
     * @param x        coordinates array
     */
    public void sUx(String fileName, double[] x) {
        for (var x_i : x) {
            assert (area.xLeft() <= x_i && x_i <= area.xRight());
        }

        var sb = new StringBuilder();
        for (int i = 0; i <= area.t().n(); i++) {
            sb.append(area.t().x(i));
            for (var x_i : x) {
                sb.append(" ").append(solution.get(i, area.x().i(x_i)));
            }
            sb.append("\n");
        }
        FileUtil.serialize(fileName, sb);
    }

    /**
     * Get slice U(x) for definite time which corresponds to layer `it` in solution matrix
     *
     * @param it index of time layer in solution matrix
     * @return U(x) slice
     */
    protected Matrix gUt(int it) {
        int M = solution.getM();
        assert (0 <= it && it < M);

        int N = solution.getN();
        var matrix = new Matrix(2, N);
        for (int i = 0; i < N; i++) {
            matrix.setX(i, area.x().x(i));
            matrix.setY(i, solution.get(it, i));
        }
        return matrix;
    }

    /**
     * Get slice U(x) for definite time
     *
     * @param t time
     * @return U(x) slice
     */
    protected Matrix gUt(double t) {
        return gUt(area.t().i(t));
    }

    /**
     * Get slice U(t) for definite space coordinate which corresponds to column `ix` in solution matrix
     *
     * @param ix index of space column in solution matrix
     * @return U(t) slice
     */
    protected Matrix gUx(int ix) {
        int N = solution.getN();
        assert (0 <= ix && ix < N);

        int M = solution.getM();
        var matrix = new Matrix(2, M);
        for (int i = 0; i <= M; i++) {
            matrix.setX(i, area.t().x(i));
            matrix.setY(i, solution.get(i, ix));
        }
        return matrix;
    }

    /**
     * Get slice U(t) for definite space coordinate
     *
     * @param x space coordinate
     * @return U(t) slice
     */
    protected Matrix gUx(double x) {
        return gUx(area.x().i(x));
    }

    /**
     * Tridiagonal matrix algorithm
     */
    protected void progonka(double[] A, double[] B, double[] C, double[] F, double m1, double n1, double m2, double n2, double[] Y) {
        int N = A.length;
        double[] Alpha = new double[N + 1];
        double[] Beta = new double[N + 1];

        Alpha[1] = m1;
        Beta[1] = n1;
        for (int i = 1; i < N; i++) {
            Alpha[i + 1] = B[i] / (C[i] - A[i] * Alpha[i]);
            Beta[i + 1] = (A[i] * Beta[i] + F[i]) / (C[i] - A[i] * Alpha[i]);
        }

        Y[N] = (n2 + m2 * Beta[N]) / (1 - m2 * Alpha[N]);
        for (int i = N - 1; i >= 0; i--) {
            Y[i] = Alpha[i + 1] * Y[i + 1] + Beta[i + 1];
        }
    }
}
