package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.pde.border.BorderCondition;
import by.andd3dfx.math.pde.border.BorderConditionType1;
import by.andd3dfx.math.pde.border.BorderConditionType2;
import by.andd3dfx.math.pde.border.BorderConditionType3;

/**
 * Parabolic equation (described heat/mass transfer):
 * M(x,t,U)*dU_dt = dU(K(x,t,U)*dU_dx)_dx + V(x,t,U)*dU_dx + F(x,t,U) where U = U(x,t)
 */
public class ParabolicEquation extends Equation {

    /**
     * Create parabolic equation
     *
     * @param x1                   left space coordinate
     * @param x2                   right space coordinate
     * @param t2                   right time coordinate
     * @param leftBorderCondition  left border condition
     * @param rightBorderCondition right border condition
     */
    public ParabolicEquation(double x1, double x2, double t2,
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
    public void solve(double h, double tau) {
        prepare(h, tau);

        int N = area.x().n();
        var A = new double[N];
        var B = new double[N];
        var C = new double[N];
        var F = new double[N];
        var U = new double[N + 1];      // Coefficients for tridiagonal matrix algorithm
        double
                _2h2 = 2 * h * h,
                _2h2_tau = _2h2 / tau;

        // Finite-difference algorithm implementation
        //
        for (int j = 0; j < area.t().n(); j++) {
            for (int i = 1; i < N; i++) {
                double
                        _u = arr.get(j, i - 1),
                        u = arr.get(j, i),
                        u_ = arr.get(j, i + 1),

                        _x = area.x().x(i - 1),
                        x = area.x().x(i),
                        x_ = area.x().x(i + 1),
                        t = area.t().x(j),

                        Alpha = (gK(x, t, u) + gK(x_, t, u_) + gV(x, t, u) * h) / 2.,
                        Beta = (gK(x, t, u) + gK(_x, t, _u) - gV(x, t, u) * h) / 2.,
                        Gamma = _2h2_tau * gM(x, t, u);

                A[i] = Beta;
                B[i] = Alpha;
                C[i] = Alpha + Beta + Gamma;
                F[i] = u_ * Alpha + _u * Beta - u * (Alpha + Beta - Gamma) + _2h2 * gF(x, t, u);
            }

            int nj = j + 1;
            double[] Mu = new double[3];
            double[] Nu = new double[3];
            double t = area.t().x(nj);

            if (leftBorderCondition instanceof BorderConditionType1 condition) {
                Nu[1] = condition.gU(t);
            } else if (leftBorderCondition instanceof BorderConditionType2 condition) {
                Mu[1] = 1;
                Nu[1] = -h * condition.gdU_dx(t);
            } else if (leftBorderCondition instanceof BorderConditionType3 condition) {
                var lh = condition.gH();
                Mu[1] = 1 / (1 + h * lh);
                Nu[1] = h * lh * condition.gTheta(t) / (1 + h * lh);
            }

            if (rightBorderCondition instanceof BorderConditionType1 condition) {
                Nu[2] = condition.gU(t);
            } else if (rightBorderCondition instanceof BorderConditionType2 condition) {
                Mu[2] = 1;
                Nu[2] = h * condition.gdU_dx(t);
            } else if (rightBorderCondition instanceof BorderConditionType3 condition) {
                var rh = condition.gH();
                Mu[2] = 1 / (1 - h * rh);
                Nu[2] = -h * rh * condition.gTheta(t) / (1 - h * rh);
            }

            progonka(A, B, C, F, Mu[1], Nu[1], Mu[2], Nu[2], U);
            for (int i = 0; i <= N; i++) {
                arr.set(nj, i, U[i]);
            }
        }
    }
}
