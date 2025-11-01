package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.pde.border.NeumannBorderCondition;
import io.github.andreipunko.math.pde.equation.ParabolicEquation;
import io.github.andreipunko.util.FileUtil;
import org.junit.jupiter.api.Test;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <pre>
 * Test for ParabolicEquationSolver with Neumann border conditions on both sides.
 *
 * Solution of diffusion equation: Ut = D*Uxx
 * - sealed pipe ends, so no diffusion through the ends
 * - initial concentration with triangle profile: most mass concentrated in the center (see method getU0(x))
 * - constant diffusion coefficient
 * </pre>
 *
 * @see <a href="https://math.libretexts.org/Bookshelves/Differential_Equations/Differential_Equations_(Chasnov)/09%3A_Partial_Differential_Equations/9.05%3A_Solution_of_the_Diffusion_Equation">article</a>
 */
class ParabolicEquationSolverNNTest {

    private final double C_MAX = 100.0;     // Max concentration
    private final double L = 0.001;         // Thickness of plate, m
    private final double TIME = 1;          // Investigated time, sec
    private final double D = 1e-9;          // Diffusion coefficient

    private final double h = L / 100.0;         // Space step
    private final double tau = TIME / 100.0;    // Time step

    // We allow difference between numeric & analytic solution no more than 1% of max concentration value
    private final double EPSILON = C_MAX / 100.;

    @Test
    void solve() {
        var diffusionEquation = buildParabolicEquation();

        // Solve equation
        var solution = new ParabolicEquationSolver()
                .solve(diffusionEquation, h, tau);

        // Save numeric solution to file
        var numericU = solution.gUt(TIME);
        FileUtil.save(numericU, "./build/parabolic22-numeric.txt", true);

        // Save analytic solution to file
        FileUtil.saveFunc(solution.area().x(), (x) -> analyticSolution(x, TIME), "./build/parabolic22-analytic.txt");

        // Compare numeric & analytic solutions
        for (var i = 0; i < numericU.getN(); i++) {
            var x = numericU.x(i);
            var numericY = numericU.y(i);
            var analyticY = analyticSolution(x, TIME);
            assertThat(Math.abs(numericY - analyticY)).isLessThanOrEqualTo(EPSILON);
        }
    }

    private ParabolicEquation buildParabolicEquation() {
        var leftBorderCondition = new NeumannBorderCondition();
        var rightBorderCondition = new NeumannBorderCondition();

        return new ParabolicEquation(0, L, TIME, leftBorderCondition, rightBorderCondition) {
            @Override
            public double gK(double x, double t, double U) {
                return D;
            }

            @Override
            public double gU0(double x) {
                return getU0(x);
            }
        };
    }

    /**
     * Initial concentration profile
     *
     * @param x space coordinate
     * @return concentration C_0(x)
     */
    private double getU0(double x) {
        x /= L;
        if (0.4 <= x && x <= 0.5) {
            return C_MAX * (10 * x - 4);
        }
        if (0.5 <= x && x <= 0.6) {
            return C_MAX * (-10 * x + 6);
        }
        return 0;
    }

    /**
     * <pre>
     * Analytic solution:
     * U(x,t) = a_0/2 + Sum( a_n*u_n(x,t) ) = a_0/2 + Sum( a_n*cos(n*PI*x/L))*exp(-(n*PI/L)^2 * D*t )
     *
     * According to <a href="https://math.libretexts.org/Bookshelves/Differential_Equations/Differential_Equations_(Chasnov)/09%3A_Partial_Differential_Equations/9.05%3A_Solution_of_the_Diffusion_Equation">article</a>
     * </pre>
     *
     * @param x space coordinate
     * @param t time
     * @return concentration C(x,t)
     */
    private double analyticSolution(double x, double t) {
        var result = 0d;
        for (int n = 1; n <= 100; n++) {
            result += a_n(n) * cos(n * PI * x / L) * exp(-Math.pow(n * PI / L, 2.) * D * t);
        }
        return a_n(0) / 2. + result;
    }

    /**
     * Coefficient a_n of a Fourier sine series
     *
     * @param n number of coefficient
     * @return b_n value
     */
    private double a_n(int n) {
        var integral = 0d;
        var N = 100d;
        var dx = L / N;
        for (int i = 0; i < N; i++) {
            var x = dx * (i + 0.5);
            integral += getU0(x) * cos(n * PI * x / L) * dx;
        }
        return 2. / L * integral;
    }
}
