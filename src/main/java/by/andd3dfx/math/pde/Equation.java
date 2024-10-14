package by.andd3dfx.math.pde;

import by.andd3dfx.math.Area;
import by.andd3dfx.math.Interval;
import by.andd3dfx.math.Matrix;
import by.andd3dfx.util.FileUtil;

public abstract class Equation {

    protected int lbt;
    protected int rbt;
    protected double lh;
    protected double rh;
    protected Area area;
    protected Matrix arr = new Matrix();

    public Equation(double x1, double x2, double t2, int lbt, int rbt, double lH, double rH) {
        assert (lbt >= 1 && lbt <= 3 && rbt >= 1 && rbt <= 3 && lH > 0 && rH > 0);

        area = new Area(new Interval(x1, x2, 1), new Interval(0, t2, 1));
        this.lbt = lbt;
        this.rbt = rbt;
        this.lh = lH;
        this.rh = rH;
    }

    public Equation(double x1, double x2, double t2) {
        this(x1, x2, t2, 1, 1, 1, 1);
    }

    protected double gU0(double x) {
        return 0;
    }

    protected double gLU(double t) {
        return 0;
    }

    protected double gRU(double t) {
        return 0;
    }

    protected double gLdU_dx(double t) {
        return 0;
    }

    protected double gRdU_dx(double t) {
        return 0;
    }

    protected double gLTeta(double t) {
        return 0;
    }

    protected double gRTeta(double t) {
        return 0;
    }

    protected double gM(double x, double t, double U) {
        return 1;
    }

    protected double gK(double x, double t, double U) {
        return 1;
    }

    protected double gV(double x, double t, double U) {
        return 0;
    }

    protected double gF(double x, double t, double U) {
        return 0;
    }

    public void solve(double h, double tau) {
        assert (h > 0 && tau > 0);                      // установка шагов по пространственной и временной координатам
        area.x().reborn(area.x().left(), area.x().right(), h);
        area.t().reborn(area.t().left(), area.t().right(), tau);

        arr = new Matrix(area.t().n() + 1, area.x().n() + 1); // Место для решения уравнения
        for (var i = 0; i <= area.x().n(); i++) {
            arr.setX(i, gU0(area.x().x(i)));            // задание начального значения
        }
    }

    public void sUt(String fileName, double t) {
        sUt(fileName, new double[]{0, t}, 1);
    }

    public void sUt(String fileName, double t[], int size) {
        for (var i = 0; i < size; i++) {
            var tInterval = area.t();
            assert (tInterval.left() <= t[i] && t[i] <= tInterval.right());
        }

        var sb = new StringBuilder();
        for (var i = 0; i <= area.x().n(); i++) {
            sb.append(area.x().x(i));
            for (int j = 0; j < size; j++) {
                sb.append(" " + arr.data(area.t().i(t[j]), i));
            }
            sb.append("\n");
        }
        FileUtil.serialize(fileName, sb);
    }

    public void sUx(String fileName, double x) {
        sUx(fileName, new double[]{0, x}, 1);
    }

    public void sUx(String fileName, double x[], int size) {
        for (int i = 0; i < size; i++) {
            assert (area.x().left() <= x[i] && x[i] <= area.x().right());
        }

        var sb = new StringBuilder();
        for (int i = 0; i <= area.t().n(); i++) {
            sb.append(area.t().x(i));
            for (int j = 0; j < size; j++) {
                sb.append(" " + arr.data(i, area.x().i(x[j])));
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
     * Получение среза U(x) при заданном t
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
     * Получение среза U(t) при заданном x
     */
    protected Matrix gUx(double x) {
        return gUx(area.x().i(x));
    }

    protected void progonka(int N, double[] A, double[] B, double[] C, double[] F, double m1, double n1, double m2, double n2, double[] Y) {
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
