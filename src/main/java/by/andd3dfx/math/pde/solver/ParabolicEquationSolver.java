package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.Area;
import by.andd3dfx.math.Interval;
import by.andd3dfx.math.pde.equation.ParabolicEquation;

/**
 * Parabolic equation solver
 *
 * @see ParabolicEquation
 */
public class ParabolicEquationSolver extends AbstractEquationSolver<ParabolicEquation> {

    /**
     * Solve parabolic equation using provided space and time steps
     *
     * @param eqn partial difference equation
     * @param h   space step
     * @param tau time step
     * @return equation solution
     */
    @Override
    public Solution<ParabolicEquation> solve(ParabolicEquation eqn, double h, double tau) {
        var area = new Area(
                new Interval(eqn.getX1(), eqn.getX2(), h),
                new Interval(0, eqn.getT2(), tau)
        );
        var solution = prepare(eqn, area);

        int N = area.xn();
        var A = new double[N];
        var B = new double[N];
        var C = new double[N];
        var F = new double[N];
        double                      // To speed-up calculations & readability
                _2h2 = 2 * h * h,
                _2h2_tau = _2h2 / tau;

        // Finite-difference algorithm implementation
        //
        for (int j = 0; j < area.tn(); j++) {
            for (int i = 1; i < N; i++) {
                double
                        _u = solution.get(j, i - 1),
                        u = solution.get(j, i),
                        u_ = solution.get(j, i + 1),

                        _x = area.xx(i - 1),
                        x = area.xx(i),
                        x_ = area.xx(i + 1),
                        t = area.tx(j),

                        Alpha = (eqn.gK(x, t, u) + eqn.gK(x_, t, u_) + eqn.gV(x, t, u) * h) / 2.,
                        Beta = (eqn.gK(x, t, u) + eqn.gK(_x, t, _u) - eqn.gV(x, t, u) * h) / 2.,
                        Gamma = _2h2_tau * eqn.gM(x, t, u);

                A[i] = Beta;
                B[i] = Alpha;
                C[i] = Alpha + Beta + Gamma;
                F[i] = u_ * Alpha + _u * Beta - u * (Alpha + Beta - Gamma) + _2h2 * eqn.gF(x, t, u);
            }

            int nj = j + 1;
            var time = area.tx(nj);
            var kappaNuLeft = calcKappaNu(eqn.getLeftBorderCondition(), h, time);
            var kappaNuRight = calcKappaNu(eqn.getRightBorderCondition(), h, time);
            var U = solve3DiagonalEquationsSystem(A, B, C, F, kappaNuLeft, kappaNuRight);
            solution.set(nj, U);
        }
        return new Solution<>(eqn, area, solution);
    }
}
