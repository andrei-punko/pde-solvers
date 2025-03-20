package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.pde.equation.HyperbolicEquation;

/**
 * Solver for hyperbolic partial differential equations.
 * Implements numerical method for solving hyperbolic equations using
 * an implicit finite difference scheme. The algorithm is based on
 * the three-layer scheme with weights for time discretization.
 *
 * @see HyperbolicEquation
 * @see AbstractEquationSolver
 */
public class HyperbolicEquationSolver extends AbstractEquationSolver<HyperbolicEquation> {

    /**
     * Solves hyperbolic partial differential equation using numerical method.
     * The solution is found using a three-layer implicit finite difference scheme.
     * The algorithm consists of two main steps:
     * <ol>
     *   <li>Calculation of the first time layer using initial conditions and their derivatives</li>
     *   <li>Solution of the three-layer implicit scheme for subsequent time layers</li>
     * </ol>
     *
     * @param eqn hyperbolic partial differential equation to solve
     * @param h   spatial step size (must be positive)
     * @param tau temporal step size (must be positive)
     * @return solution containing function values at all grid points
     * @throws IllegalArgumentException if parameters h or tau are non-positive
     */
    @Override
    public Solution<HyperbolicEquation> solve(HyperbolicEquation eqn, double h, double tau) {
        var area = buildArea(eqn, h, tau);
        var solution = prepare(eqn, area);

        int N = area.xn();
        var A = new double[N];
        var B = new double[N];
        var C = new double[N];
        var F = new double[N];
        double _2h = 2 * h,           // To speed-up calculations & readability
                h2 = h * h,
                t_2 = tau / 2.,
                h_2 = h / 2.,
                h2_tau = h2 / tau,
                _2h2_tau2 = 2 * Math.pow(h / tau, 2);

        // Set border conditions on layer 1
        solution.set(1, 0, calcFirstLayerValue(eqn, tau, solution.get(0, 0), area.xLeft()));
        solution.set(1, N, calcFirstLayerValue(eqn, tau, solution.get(0, N), area.xRight()));

        // Calculate U value on layer 1 which needed to start finite-difference algorithm
        //
        for (int i = 1; i < N; i++) {
            double
                    _u = solution.get(0, i - 1),
                    u = solution.get(0, i),
                    u_ = solution.get(0, i + 1),
                    x = area.xx(i);

            solution.set(1, i, u + tau * (eqn.gdU_dt0(x) + t_2 / eqn.gM(x, 0, u) * (
                    eqn.gK(x, 0, u) / h2 * (_u - 2 * u + u_) + eqn.gV(x, 0, u) / _2h * (u_ - _u) + eqn.gF(x, 0, u))));
        }

        // Finite-difference algorithm implementation
        //
        for (int j = 0; j <= area.tn() - 2; j++) {
            for (int i = 1; i < N; i++) {
                double
                        _u = solution.get(j, i - 1),
                        u = solution.get(j, i),
                        u_ = solution.get(j, i + 1),

                        x = area.xx(i),
                        t = area.tx(j),

                        Alpha = eqn.gK(x, t, u) - eqn.gV(x, t, u) * h_2,
                        Beta = eqn.gK(x, t, u) + eqn.gV(x, t, u) * h_2,
                        Gamma = h2_tau * eqn.gL(x, t, u),
                        Delta = _2h2_tau2 * eqn.gM(x, t, u);

                A[i] = Alpha;
                B[i] = Beta;
                C[i] = Alpha + Beta - Gamma + Delta;
                F[i] = _u * Alpha + u_ * Beta - u * (Alpha + Beta + Gamma + Delta)
                        + 2 * (solution.get(j + 1, i) * Delta + eqn.gF(x, t, u) * h2);
            }

            int nj = j + 2;
            var time = area.tx(nj);
            var kappaNuLeft = calcKappaNu(eqn.getLeftBorderCondition(), h, time);
            var kappaNuRight = calcKappaNu(eqn.getRightBorderCondition(), h, time);
            var U = solve3DiagonalEquationsSystem(A, B, C, F, kappaNuLeft, kappaNuRight);
            solution.set(nj, U);
        }
        return new Solution<>(eqn, area, solution);
    }

    /**
     * Calculates the solution value at the first time layer for a given spatial point.
     * This calculation uses the initial conditions and their derivatives to approximate
     * the solution at time t = tau.
     *
     * @param eqn hyperbolic equation to solve
     * @param tau temporal step size
     * @param u   initial value at the point
     * @param x   spatial coordinate
     * @return approximated solution value at the first time layer
     */
    private double calcFirstLayerValue(HyperbolicEquation eqn, double tau, double u, double x) {
        return u + tau * (eqn.gdU_dt0(x) + tau / 2. / eqn.gM(x, 0, u) * eqn.gF(x, 0, u));
    }
}
