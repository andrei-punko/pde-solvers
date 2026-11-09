package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.pde.border.DirichletBorderCondition;
import io.github.andreipunko.math.pde.equation.HyperbolicEquation;
import io.github.andreipunko.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <pre>
 * Test for HyperbolicEquatorSolver with Dirichlet border conditions on both sides
 * and a damping term L(x,t,U) = 2*GAMMA != 0.
 *
 * Solution of damped wave equation: Utt + 2*GAMMA*Ut = Uxx
 * - constant displacement U=0 on the left & right borders
 * - initial displacement U(x,0) = sin(x): a single exact eigenfunction of d2/dx2
 *   on [0, PI], so the solution stays a single mode and no Fourier series is needed
 * - zero initial velocity Ut(x,0) = 0
 * </pre>
 *
 * Exact single-mode solution (standard damped oscillator per spatial eigenmode):
 * <pre>
 *   U(x,t) = exp(-GAMMA*t) * sin(x) * ( cos(w*t) + GAMMA/w * sin(w*t) ),   w = sqrt(1 - GAMMA^2)
 * </pre>
 *
 * Regression guard for the sign of the L(x,t,U)*Ut term: the L coefficient used to enter the
 * three-layer sweep with the wrong sign, which turned damping into anti-damping (the numerical
 * amplitude grew exponentially instead of decaying).
 *
 * @see <a href="https://github.com/andrei-punko/pde-solvers/issues/16">issue #16</a>
 * @see <a href="https://math.libretexts.org/Bookshelves/Differential_Equations/Differential_Equations_(Chasnov)/09%3A_Partial_Differential_Equations/9.06%3A_Solution_of_the_Wave_Equation">article</a>
 */
class HyperbolicEquationSolverDampedDDTest {

    private final double GAMMA = 0.3;   // Damping coefficient: equation is Utt + 2*GAMMA*Ut = Uxx
    private final double L = PI;        // Length of string
    private final double TIME = 8;      // Investigated time, sec

    private final double h = PI / 200.0;        // Space step
    private final double tau = TIME / 2000.0;   // Time step

    private final double W = sqrt(1 - GAMMA * GAMMA);   // Damped oscillation frequency

    // We allow difference between numeric & analytic solution no more than 1% of U_MAX value.
    // Measured calibration error on this grid is ~4.4e-3, dominated by the O(tau) first-layer
    // start-up approximation (it ignores the damping term over the first time step);
    // the ceiling leaves >2x margin. The exact-solution comparison is not the primary guard here:
    // the amplitude assertion below catches the anti-damping regression (which yields exponential
    // growth) orders of magnitude above this tolerance.
    private final double EPSILON = 1e-2;

    @Test
    void solve() throws IOException {
        var hyperbolicEquation = buildHyperbolicEquation();

        // Solve equation
        var solution = new HyperbolicEquationSolver()
                .solve(hyperbolicEquation, h, tau);

        // Save numeric solution to file
        var numericU = solution.gUt(TIME);
        FileUtil.save(numericU, "./build/hyperbolic21-damped-numeric.txt", true);

        // Save analytic solution to file
        FileUtil.saveFunc(solution.area().x(), (x) -> analyticSolution(x, TIME), "./build/hyperbolic21-damped-analytic.txt");

        // Compare numeric & analytic solutions, tracking the reached amplitude:
        // a damped wave decayed ~exp(-GAMMA*t) must stay small (anti-damping would blow it up)
        double maxAbsErr = 0d;
        double maxNumeric = 0d;
        for (var i = 0; i < numericU.getN(); i++) {
            var x = numericU.x(i);
            var numericY = numericU.y(i);
            var analyticY = analyticSolution(x, TIME);
            maxAbsErr = Math.max(maxAbsErr, Math.abs(numericY - analyticY));
            maxNumeric = Math.max(maxNumeric, Math.abs(numericY));
        }
        // Peak of |U| at t = TIME is bounded by exp(-GAMMA*TIME) ≈ 0.09; use generous ceiling
        assertThat(maxNumeric).isLessThanOrEqualTo(3 * exp(-GAMMA * TIME));
        assertThat(maxAbsErr).isLessThanOrEqualTo(EPSILON);
    }

    private HyperbolicEquation buildHyperbolicEquation() {
        var leftBorderCondition = new DirichletBorderCondition();
        var rightBorderCondition = new DirichletBorderCondition();

        return new HyperbolicEquation(0, L, TIME, leftBorderCondition, rightBorderCondition) {
            @Override
            public double gU0(double x) {
                return sin(x);
            }

            @Override
            public double gL(double x, double t, double U) {
                return 2 * GAMMA;
            }
        };
    }

    /**
     * <pre>
     * Analytic solution:
     * U(x,t) = exp(-GAMMA*t) * sin(x) * ( cos(W*t) + GAMMA/W * sin(W*t) )
     *
     * T(t) = exp(-GAMMA*t) * ( cos(W*t) + GAMMA/W * sin(W*t) ) is the solution of
     * T'' + 2*GAMMA*T' + T = 0 with T(0) = 1, T'(0) = 0.
     * </pre>
     *
     * @param x space coordinate
     * @param t time
     * @return displacement U(x,t)
     */
    private double analyticSolution(double x, double t) {
        var envelope = exp(-GAMMA * t);
        var oscillator = cos(W * t) + GAMMA / W * sin(W * t);
        return envelope * oscillator * sin(x);
    }
}
