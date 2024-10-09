package by.andd3dfx.math.pde;

import by.andd3dfx.math.Area;
import by.andd3dfx.math.Interval;
import by.andd3dfx.math.Matrix;
import by.andd3dfx.util.FileUtil;

public abstract class Equation {

    protected int lbt, rbt;
    protected double lh, rh;
    protected Area area;
    protected Matrix arr = new Matrix();

    public Equation(double x1, double x2, double t2, int l, int r, double lH, double rH) {
        assert (l >= 1 && l <= 3 && r >= 1 && r <= 3 && lH > 0 && rH > 0);

        area = new Area(new Interval(x1, x2, 1), new Interval(0, t2, 1));
        lbt = l;
        rbt = r;
        lh = lH;
        rh = rH;
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
        area.x().reborn(area.x().getX1(), area.x().getX2(), h);
        area.t().reborn(area.t().getX1(), area.t().getX2(), tau);

        arr = new Matrix(area.t().getN() + 1, area.x().getN() + 1); // Место для решения уравнения
        for (var i = 0; i <= area.x().getN(); i++) {
            arr.setX(i, gU0(area.x().x(i)));            // задание начального значения
        }
    }

    public void sUt(String fileName, double t) {
        sUt(fileName, new double[]{0, t}, 1);
    }

    public void sUt(String fileName, double t[], int size) {
        for (var i = 0; i < size; i++) {
            var tInterval = area.t();
            assert (tInterval.getX1() <= t[i] && t[i] <= tInterval.getX2());
        }

        var sb = new StringBuilder();
        for (var i = 0; i <= area.x().getN(); i++) {
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
            assert (area.x().getX1() <= x[i] && x[i] <= area.x().getX2());
        }

        var sb = new StringBuilder();
        for (int i = 0; i <= area.t().getN(); i++) {
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

    protected void progonka(int N, Matrix A, Matrix B, Matrix C, Matrix F, double m1, double n1, double m2, double n2, Matrix Y) {
        Matrix Alpha = new Matrix(N + 1);
        Matrix Beta = new Matrix(N + 1);

        Alpha.set(1, m1);
        Beta.set(1, n1);
        for (int i = 1; i < N; i++) {
            Alpha.set(i + 1, B.get(i) / (C.get(i) - A.get(i) * Alpha.get(i)));
            Beta.set(i + 1, (A.get(i) * Beta.get(i) + F.get(i)) / (C.get(i) - A.get(i) * Alpha.get(i)));
        }

        Y.set(N, (n2 + m2 * Beta.get(N)) / (1 - m2 * Alpha.get(N)));
        for (int i = N - 1; i >= 0; i--) {
            Y.set(i, Alpha.get(i + 1) * Y.get(i + 1) + Beta.get(i + 1));
        }
    }
}
