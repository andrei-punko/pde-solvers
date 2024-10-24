package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.pde.border.BorderConditionType1;
import by.andd3dfx.util.FileUtil;
import org.junit.jupiter.api.Test;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <pre>
 * Solution of wave equation: Utt = c^2*Uxx
 * - constant displacement U=0 on the left & right borders
 * - initial displacement with triangle profile (see method getU0(x))
 * - constant coefficient c
 * </pre>
 *
 * @see <a href="https://math.libretexts.org/Bookshelves/Differential_Equations/Differential_Equations_(Chasnov)/09%3A_Partial_Differential_Equations/9.06%3A_Solution_of_the_Wave_Equation">article</a>
 */
class HyperbolicEquationTest {

    private final double U_MAX = 0.005;     // Max displacement, m
    private final double L = 0.100;         // Length of string, m
    private final double TIME = 25;         // Investigated time, sec
    private final double C_coeff = 1e-2;    // c^2 = T/Ro

    private final double h = L / 2000.0;
    private final double tau = TIME / 1000.0;

    // We allow difference between numeric & analytic solution no more than 3% of max displacement value
    private final double EPSILON = U_MAX / 33.;

    @Test
    void solve() {
        var waveEquation = buildHyperbolicEquation();

        // Solve equation
        waveEquation.solve(h, tau);

        // Save numeric solution to file
        var numericU = waveEquation.gUt(TIME);
        FileUtil.save(numericU, "./build/hyperbolic-numeric.txt", true);

        // Save analytic solution to file
        FileUtil.saveFunc(waveEquation.area.x(), (x) -> analyticSolution(x, TIME), "./build/hyperbolic-analytic.txt");

        // Compare numeric & analytic solutions
        for (var i = 0; i < numericU.getN(); i++) {
            var x = numericU.x(i);
            var numericY = numericU.y(i);
            var analyticY = analyticSolution(x, TIME);
            assertThat(Math.abs(numericY - analyticY)).isLessThanOrEqualTo(EPSILON);
        }
    }

    private HyperbolicEquation buildHyperbolicEquation() {
        var leftBorderCondition = new BorderConditionType1();
        var rightBorderCondition = new BorderConditionType1();

        return new HyperbolicEquation(0, L, TIME, leftBorderCondition, rightBorderCondition) {
            @Override
            protected double gK(double x, double t, double U) {
                return C_coeff * C_coeff;
            }

            @Override
            protected double gU0(double x) {
                return getU0(x);
            }
        };
    }

    /**
     * Initial displacement profile
     *
     * @param x space coordinate
     * @return displacement U_0(x)
     */
    private double getU0(double x) {
        x /= L;
        if (0 <= x && x <= 0.2) {
            return U_MAX * 5 * x;
        }
        return U_MAX * 1.25 * (1 - x);
    }

    /**
     * <pre>
     * Analytic solution:
     * U(x,t) = Sum(b_n*u_n(x,t)) = Sum(b_n*sin(n*PI*x/L)*cos(n*PI*C*t/L))
     *
     * According to <a href="https://math.libretexts.org/Bookshelves/Differential_Equations/Differential_Equations_(Chasnov)/09%3A_Partial_Differential_Equations/9.06%3A_Solution_of_the_Wave_Equation">article</a>
     * </pre>
     *
     * @param x space coordinate
     * @param t time
     * @return concentration C(x,t)
     */
    private double analyticSolution(double x, double t) {
        var result = 0d;
        for (int n = 1; n <= 100; n++) {
            result += b(n) * sin(n * PI * x / L) * cos(n * PI * C_coeff * t / L);
        }
        return result;
    }

    /**
     * Coefficient b_n of a Fourier sine series
     *
     * @param n number of coefficient
     * @return b_n value
     */
    private double b(int n) {
        var integral = 0d;
        var N = 100d;
        var dx = L / N;
        for (int i = 0; i < N; i++) {
            var x = dx * (i + 0.5);
            integral += getU0(x) * sin(n * PI * x / L) * dx;
        }
        return 2. / L * integral;
    }
}
