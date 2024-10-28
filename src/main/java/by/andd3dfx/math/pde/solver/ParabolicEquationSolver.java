package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.Area;
import by.andd3dfx.math.Interval;
import by.andd3dfx.math.pde.equation.ParabolicEquation;

public class ParabolicEquationSolver extends AbstractEquationSolver<ParabolicEquation> {

    @Override
    public Solution<ParabolicEquation> solve(ParabolicEquation eqn, double h, double tau) {
        var area = new Area(
                new Interval(eqn.getX1(), eqn.getX2(), h),
                new Interval(0, eqn.getT2(), tau)
        );
        var solution = prepare(eqn, area);

        int N = area.x().n();
        var A = new double[N];
        var B = new double[N];
        var C = new double[N];
        var F = new double[N];
        double                      // To speed-up calculations & readability
                _2h2 = 2 * h * h,
                _2h2_tau = _2h2 / tau;

        // Finite-difference algorithm implementation
        //
        for (int j = 0; j < area.t().n(); j++) {
            for (int i = 1; i < N; i++) {
                double
                        _u = solution.get(j, i - 1),
                        u = solution.get(j, i),
                        u_ = solution.get(j, i + 1),

                        _x = area.x().x(i - 1),
                        x = area.x().x(i),
                        x_ = area.x().x(i + 1),
                        t = area.t().x(j),

                        Alpha = (eqn.gK(x, t, u) + eqn.gK(x_, t, u_) + eqn.gV(x, t, u) * h) / 2.,
                        Beta = (eqn.gK(x, t, u) + eqn.gK(_x, t, _u) - eqn.gV(x, t, u) * h) / 2.,
                        Gamma = _2h2_tau * eqn.gM(x, t, u);

                A[i] = Beta;
                B[i] = Alpha;
                C[i] = Alpha + Beta + Gamma;
                F[i] = u_ * Alpha + _u * Beta - u * (Alpha + Beta - Gamma) + _2h2 * eqn.gF(x, t, u);
            }

            int nj = j + 1;
            var time = area.t().x(nj);
            var U = progonka(eqn, h, time, A, B, C, F);
            solution.set(nj, U);
        }
        return new Solution(eqn, area, solution);
    }
}