package by.andd3dfx.math.pde;

/**
 * Parabolic equation:
 * M(x,t,U)*d2U_dt2 + L(x,t,U)*dU_dt = K(x,t,U)*d2U_dt2 + V(x,t,U)*dU_dt + F(x,t,U) where U = U(x,t)
 */
public class HyperbolicEquation extends Equation {

    /**
     * Create hyperbolic equation
     *
     * @param x1  left space coordinate
     * @param x2  right space coordinate
     * @param t2  right time coordinate
     * @param lbt type of left border condition (1/2/3)
     * @param rbt type of right border condition (1/2/3)
     * @param lH  coefficient for 3rd border condition type of left border
     * @param rH  coefficient for 3rd border condition type of right border
     */
    public HyperbolicEquation(double x1, double x2, double t2, int lbt, int rbt, double lH, double rH) {
        super(x1, x2, t2, lbt, rbt, lH, rH);
    }

    /**
     * Create hyperbolic equation
     *
     * @param x1 left space coordinate
     * @param x2 right space coordinate
     * @param t2 right time coordinate
     */
    public HyperbolicEquation(double x1, double x2, double t2) {
        this(x1, x2, t2, 1, 1, 1, 1);
    }

    /**
     * Solve equation using provided space & time steps
     *
     * @param h   space step
     * @param tau time step
     */
    @Override
    public void solve(double h, double tau) {
        prepare(h, tau);

        int N = area.x().n();
        var A = new double[N];
        var B = new double[N];
        var C = new double[N];
        var F = new double[N];
        var U = new double[N + 1];    // Коэффициенты для метода прогонки

        double _2h = 2 * h,                        //Для ускорения вычислений
                h2 = h * h,
                t_2 = tau / 2.,
                h_2 = h / 2.,
                h2_tau = h2 / tau,
                _2h2_tau2 = 2 * Math.pow(h / tau, 2);

        arr.set(1, 0, gLU(tau));        //Задание граничных значений на первом слое
        arr.set(1, N, gRU(tau));

        // Вычисление значения функции на первом слое для запуска разностной схемы
        //
        for (int i = 1; i < N; i++) {
            double
                    _u = arr.data(0, i - 1),
                    u = arr.data(0, i),
                    u_ = arr.data(0, i + 1),
                    x = area.x().x(i);

            arr.set(1, i, u + tau * (gdU_dt(x) + t_2 / gM(x, 0, u) * (
                    gK(x, 0, u) / h2 * (_u - 2 * u + u_) + gV(x, 0, u) / _2h * (u_ - _u) + gF(x, 0, u))));
        }

        // Реализация разностной схемы
        //
        for (int j = 0; j <= area.t().n() - 2; j++) {
            for (int i = 1; i < N; i++) {
                double
                        _u = arr.data(j, i - 1),
                        u = arr.data(j, i),
                        u_ = arr.data(j, i + 1),

                        x = area.x().x(i),
                        t = area.t().x(j),

                        Alpha = gK(x, t, u) - gV(x, t, u) * h_2,
                        Beta = gK(x, t, u) + gV(x, t, u) * h_2,
                        Gamma = h2_tau * gL(x, t, u),
                        Delta = _2h2_tau2 * gM(x, t, u);

                A[i] = Alpha;
                B[i] = Beta;
                C[i] = Alpha + Beta - Gamma + Delta;
                F[i] = _u * Alpha + u_ * Beta - u * (Alpha + Beta + Gamma + Delta) + 2 * (arr.data(j + 1, i) * Delta + gF(x, t, u) * h2);
            }

            int nj = j + 2;
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
                    break;    //Надо бы ускорить (вынесением)
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
                arr.set(nj, i, U[i]);
            }
        }
    }

    /**
     * Initial condition dU_dt(x,0) at moment t=0
     */
    protected double gdU_dt(double x) {
        return 0;
    }

    /**
     * Coefficient L(x,t,U) of equation for 1st-order time derivative
     */
    protected double gL(double x, double t, double U) {
        return 0;
    }
}
