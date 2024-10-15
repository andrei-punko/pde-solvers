package by.andd3dfx.math.pde;

import by.andd3dfx.math.Area;
import by.andd3dfx.math.Interval;
import by.andd3dfx.math.Matrix;
import by.andd3dfx.util.FileUtil;

/**
 * Base class. Used to avoid code duplication in child classes
 */
public abstract class Equation {

    protected Area area;
    protected Matrix arr = new Matrix();
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
        area = new Area(new Interval(x1, x2, 1), new Interval(0, t2, 1));
        this.leftBorderCondition = leftBorderCondition;
        this.rightBorderCondition = rightBorderCondition;
    }

    /**
     * Initial condition U(x,0) at moment t=0
     *
     * @param x coordinate
     * @return U value in asked coordinate
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

    protected void prepare(double h, double tau) {
        assert (h > 0 && tau > 0);                      // установка шагов по пространственной и временной координатам
        area.x().reborn(area.x().left(), area.x().right(), h);
        area.t().reborn(area.t().left(), area.t().right(), tau);

        arr = new Matrix(area.t().n() + 1, area.x().n() + 1); // Место для решения уравнения
        for (var i = 0; i <= area.x().n(); i++) {
            arr.setX(i, gU0(area.x().x(i)));            // задание начального значения
        }
    }

    /**
     * Save data U(x,t*) for asked time moment t*
     *
     * @param fileName file name
     * @param t        time
     */
    public void sUt(String fileName, double t) {
        sUt(fileName, new double[]{t});
    }

    /**
     * Save data U(x,t_i) for asked time moments [t_i]. So in result we get some set of slices for several time moments
     *
     * @param fileName file name
     * @param t        times array
     */
    public void sUt(String fileName, double t[]) {
        for (var t_i : t) {
            var tInterval = area.t();
            assert (tInterval.left() <= t_i && t_i <= tInterval.right());
        }

        var sb = new StringBuilder();
        for (var i = 0; i <= area.x().n(); i++) {
            sb.append(area.x().x(i));
            for (var t_i : t) {
                sb.append(" " + arr.data(area.t().i(t_i), i));
            }
            sb.append("\n");
        }
        FileUtil.serialize(fileName, sb);
    }

    /**
     * Save data U(x*,t) for asked space coordinate x*
     *
     * @param fileName file name
     * @param x        space coordinate
     */
    public void sUx(String fileName, double x) {
        sUx(fileName, new double[]{x});
    }

    /**
     * Save data U(x_i,t) for asked space coordinates [x_i]. So in result we get some set of slices for several space coordinates
     *
     * @param fileName file name
     * @param x        coordinates array
     */
    public void sUx(String fileName, double x[]) {
        for (var x_i : x) {
            assert (area.x().left() <= x_i && x_i <= area.x().right());
        }

        var sb = new StringBuilder();
        for (int i = 0; i <= area.t().n(); i++) {
            sb.append(area.t().x(i));
            for (var x_i : x) {
                sb.append(" " + arr.data(i, area.x().i(x_i)));
            }
            sb.append("\n");
        }
        FileUtil.serialize(fileName, sb);
    }

    protected Matrix gUt(int it) {
        int N = arr.getN();
        assert (0 <= it && it < N);

        var m = new Matrix(2, N);
        for (int i = 0; i <= N; i++) {
            m.setX(i, area.x().x(i));
            m.setY(i, arr.data(it, i));
        }
        return m;
    }

    /**
     * Получение среза U(x,t*) при заданном t*
     */
    protected Matrix gUt(double t) {
        return gUt(area.t().i(t));
    }

    protected Matrix gUx(int ix) {
        int M = arr.getM();
        assert (0 <= ix && ix < M);

        var m = new Matrix(2, M);
        for (int i = 0; i <= M; i++) {
            m.setX(i, area.t().x(i));
            m.setY(i, arr.data(i, ix));
        }
        return m;
    }

    /**
     * Получение среза U(x*,t) при заданном x*
     */
    protected Matrix gUx(double x) {
        return gUx(area.x().i(x));
    }

    /**
     * Метод прогонки
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
