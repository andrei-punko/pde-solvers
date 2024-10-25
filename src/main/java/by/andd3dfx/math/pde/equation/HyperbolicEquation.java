package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.pde.border.BorderCondition;

/**
 * Hyperbolic equation (described oscillation processes):
 * M(x,t,U)*d2U_dt2 + L(x,t,U)*dU_dt = dU(K(x,t,U)*dU_dx)_dx + V(x,t,U)*dU_dx + F(x,t,U) where U = U(x,t)
 */
public class HyperbolicEquation extends Equation {

    /**
     * Create hyperbolic equation
     *
     * @param x1                   left space coordinate
     * @param x2                   right space coordinate
     * @param t2                   right time coordinate
     * @param leftBorderCondition  left border condition
     * @param rightBorderCondition right border condition
     */
    public HyperbolicEquation(double x1, double x2, double t2,
                              BorderCondition leftBorderCondition,
                              BorderCondition rightBorderCondition) {
        super(x1, x2, t2, leftBorderCondition, rightBorderCondition);
    }

    /**
     * Solve equation using provided space & time steps
     *
     * @param h   space step
     * @param tau time step
     */
    @Override
    public Solution solve(double h, double tau) {
        var solution = prepare(h, tau);

        int N = area.x().n();
        var A = new double[N];
        var B = new double[N];
        var C = new double[N];
        var F = new double[N];
        var U = new double[N + 1];    // Coefficients for tridiagonal matrix algorithm

        double _2h = 2 * h,           // To speed-up calculations
                h2 = h * h,
                t_2 = tau / 2.,
                h_2 = h / 2.,
                h2_tau = h2 / tau,
                _2h2_tau2 = 2 * Math.pow(h / tau, 2);

        // Set border conditions on layer 1
        solution.set(1, 0, calcFirstLayerValue(tau, solution.get(0, 0), area.xLeft()));
        solution.set(1, N, calcFirstLayerValue(tau, solution.get(0, N), area.xRight()));

        // Calculate U value on layer 1 which needed to start finite-difference algorithm
        //
        for (int i = 1; i < N; i++) {
            double
                    _u = solution.get(0, i - 1),
                    u = solution.get(0, i),
                    u_ = solution.get(0, i + 1),
                    x = area.x().x(i);

            solution.set(1, i, u + tau * (gdU_dt(x) + t_2 / gM(x, 0, u) * (
                    gK(x, 0, u) / h2 * (_u - 2 * u + u_) + gV(x, 0, u) / _2h * (u_ - _u) + gF(x, 0, u))));
        }

        // Finite-difference algorithm implementation
        //
        for (int j = 0; j <= area.t().n() - 2; j++) {
            for (int i = 1; i < N; i++) {
                double
                        _u = solution.get(j, i - 1),
                        u = solution.get(j, i),
                        u_ = solution.get(j, i + 1),

                        x = area.x().x(i),
                        t = area.t().x(j),

                        Alpha = gK(x, t, u) - gV(x, t, u) * h_2,
                        Beta = gK(x, t, u) + gV(x, t, u) * h_2,
                        Gamma = h2_tau * gL(x, t, u),
                        Delta = _2h2_tau2 * gM(x, t, u);

                A[i] = Alpha;
                B[i] = Beta;
                C[i] = Alpha + Beta - Gamma + Delta;
                F[i] = _u * Alpha + u_ * Beta - u * (Alpha + Beta + Gamma + Delta) + 2 * (solution.get(j + 1, i) * Delta + gF(x, t, u) * h2);
            }

            int nj = j + 2;
            double[] Mu = new double[3];
            double[] Nu = new double[3];
            double t = area.t().x(nj);

            useBorderConditions(h, Nu, t, Mu);

            progonka(A, B, C, F, Mu[1], Nu[1], Mu[2], Nu[2], U);
            solution.set(nj, U);
        }
        return new Solution(this, solution);
    }

    private double calcFirstLayerValue(double tau, double u, double x) {
        return u + tau * (gdU_dt(x) + tau / 2. / gM(x, 0, u) * gF(x, 0, u));
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
