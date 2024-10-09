package by.andd3dfx.math.pde;

import by.andd3dfx.math.Matrix;

public class ParabolicEquation extends Equation {

    public ParabolicEquation(double x1, double x2, double t2, int l, int r, double lH, double rH) {
        super(x1, x2, t2, l, r, lH, rH);
    }

    public ParabolicEquation(double x1, double x2, double t2) {
        super(x1, x2, t2);
    }

    @Override
    public void solve(double h, double tau) {
        super.solve(h, tau);

        int N = area.x().getN();
        Matrix A = new Matrix(N);
        Matrix B = new Matrix(N);
        Matrix C = new Matrix(N);
        Matrix F = new Matrix(N);
        Matrix U = new Matrix(N + 1);     // Коэффициенты для метода прогонки
        double _2h2 = 2 * h * h, _2h2_tau = _2h2 / tau;

        for (int j = 0; j < area.t().getN(); j++) {
            for (int i = 1; i < N; i++) {
                double
                        _u = arr.data(j, i - 1),
                        u = arr.data(j, i),
                        u_ = arr.data(j, i + 1),

                        _x = area.x().x(i - 1),
                        x = area.x().x(i),
                        x_ = area.x().x(i + 1),
                        t = area.t().x(j),

                        Alpha = (gK(x, t, u) + gK(x_, t, u_) + gV(x, t, u) * h) / 2.,
                        Beta = (gK(x, t, u) + gK(_x, t, _u) - gV(x, t, u) * h) / 2.,
                        Gamma = _2h2_tau * gM(x, t, u);

                A.set(i, Beta);
                B.set(i, Alpha);
                C.set(i, Alpha + Beta + Gamma);
                F.set(i, u_ * Alpha + _u * Beta - u * (Alpha + Beta - Gamma) + _2h2 * gF(x, t, u));
            }

            int nj = j + 1;
            double Mu[] = new double[3];
            double Nu[] = new double[3];
            double t = area.t().x(nj);

            switch (lbt) {
                case 1:
                    Nu[1] = gLU(t);
                    break;
                case 2:
                    Mu[1] = 1;
                    Nu[1] = -h * gLdU_dx(t);
                    break;
                case 3:
                    Mu[1] = 1 / (1 + h * lh);
                    Nu[1] = h * lh * gLTeta(t) / (1 + h * lh);
                    break;
            }
            switch (rbt) {
                case 1:
                    Nu[2] = gRU(t);
                    break;
                case 2:
                    Mu[2] = 1;
                    Nu[2] = h * gRdU_dx(t);
                    break;
                case 3:
                    Mu[2] = 1 / (1 - h * rh);
                    Nu[2] = -h * rh * gRTeta(t) / (1 - h * rh);
                    break;
            }

            progonka(N, A, B, C, F, Mu[1], Nu[1], Mu[2], Nu[2], U);
            for (int i = 0; i <= N; i++) {
                arr.set(nj, i, U.get(i));
            }
        }
    }
}
