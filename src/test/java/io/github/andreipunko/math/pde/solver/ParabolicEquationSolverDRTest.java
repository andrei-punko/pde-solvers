package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.pde.border.DirichletBorderCondition;
import io.github.andreipunko.math.pde.border.RobinBorderCondition;
import io.github.andreipunko.math.pde.equation.ParabolicEquation;
import io.github.andreipunko.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.PI;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <pre>
 * Test for ParabolicEquationSolver with Dirichlet & Robin border conditions.
 *
 * Solution of heat transfer equation: Ut = ALPHA*Uxx for rod with length L
 * - constant thermal diffusivity coefficient ALPHA
 * - temperature T is counted from the environment temperature
 * - constant temperature T=0 on the left border
 * - heat exchange with the environment on the right border
 * - initial temperature with triangle profile: most heat concentrated in the center, with T_MAX max value (see method getU0(x))
 * </pre>
 */
class ParabolicEquationSolverDRTest {

    private final double K = 401;           // Heat transfer rate of Cu
    private final double ALPHA = 1.11e-4;   // Thermal diffusivity coefficient of Cu
    private final double H_CONV = 4010;     // Convective heat transfer coefficient: (H/K)*L = 1, so Robin condition matters
    private final double T_MAX = 400;       // Peak value of starting temperature (triangle) profile

    private final double L = 0.1;           // Length of rod, m
    private final double TIME = 60;         // Investigated time, sec

    private final double h = L / 100.0;         // Space step
    private final double tau = TIME / 1000.0;   // Time step

    // We allow difference between numeric & analytic solution no more than 0.1% of T_MAX value
    private final double EPSILON = T_MAX / 1000.;

    @Test
    void solve() throws IOException {
        var diffusionEquation = buildParabolicEquation();

        // Solve equation
        var solution = new ParabolicEquationSolver()
                .solve(diffusionEquation, h, tau);

        // Save numeric solution to file
        var numericU = solution.gUt(TIME);
        FileUtil.save(numericU, "./build/parabolic13-numeric.txt", true);

        // Save analytic solution to file
        FileUtil.saveFunc(solution.area().x(), (x) -> analyticSolution(x, TIME), "./build/parabolic13-analytic.txt");

        // Compare numeric & analytic solutions
        for (var i = 0; i < numericU.getN(); i++) {
            var x = numericU.x(i);
            var numericY = numericU.y(i);
            var analyticY = analyticSolution(x, TIME);
            assertThat(Math.abs(numericY - analyticY)).isLessThanOrEqualTo(EPSILON);
        }
    }

    private ParabolicEquation buildParabolicEquation() {
        var leftBorderCondition = new DirichletBorderCondition();
        var rightBorderCondition = new RobinBorderCondition() {
            @Override
            public double gH() {
                return -H_CONV / K;
            }
        };

        return new ParabolicEquation(0, L, TIME, leftBorderCondition, rightBorderCondition) {
            @Override
            public double gK(double x, double t, double U) {
                return ALPHA;
            }

            @Override
            public double gU0(double x) {
                return getU0(x);
            }
        };
    }

    /**
     * Initial temperature profile
     *
     * @param x space coordinate
     * @return temperature T_0(x)
     */
    private double getU0(double x) {
        x /= L;
        if (0.4 <= x && x <= 0.5) {
            return T_MAX * (10 * x - 4);
        }
        if (0.5 <= x && x <= 0.6) {
            return T_MAX * (-10 * x + 6);
        }
        return 0;
    }

    /**
     * <pre>
     * Analytic solution:
     * U(x,t) = Sum( b_n*u_n(x,t) ) = Sum( b_n*sin(lambda_n*x)*exp(-alpha*lambda_n^2 *t) )
     * </pre>
     *
     * @param x space coordinate
     * @param t time
     * @return temperature T(x,t)
     */
    private double analyticSolution(double x, double t) {
        var result = 0d;
        for (int n = 1; n <= 100; n++) {
            var lambda = lambda_n(n);
            result += b_n(n) * sin(lambda * x) * exp(-ALPHA * pow(lambda, 2.) * t);
        }
        return result;
    }

    /**
     * Coefficient b_n of eigenfunction expansion: b_n = Integral( U0*sin(lambda_n*x) ) / Integral( sin^2(lambda_n*x) )
     *
     * @param n number of coefficient
     * @return b_n value
     */
    private double b_n(int n) {
        var integral = 0d;
        var N = 100d;
        var dx = L / N;
        for (int i = 0; i < N; i++) {
            var x = dx * (i + 0.5);
            integral += getU0(x) * sin(lambda_n(n) * x) * dx;
        }
        var lambda = lambda_n(n);
        var norm = L / 2 - sin(2 * lambda * L) / (4 * lambda);  // Integral of sin^2(lambda*x) over [0,L]
        return integral / norm;
    }

    /**
     * Eigenvalue lambda_n of transcendent equation: lambda = -H_CONV/K * tan( lambda*L )
     *
     * @param n number of coefficient
     * @return lambda_n value
     */
    private double lambda_n(int n) {
        if (lambdaCache.containsKey(n)) {
            return lambdaCache.get(n);
        }

        var lambda = PI / L * (n - 0.5);  // n-th root lies in ((n-1/2)*PI/L, n*PI/L)
        while (true) {
            lambda += 0.5 * PI / L / 10000.;
            if (lambda + H_CONV / K * tan(lambda * L) > 0) {
                lambdaCache.put(n, lambda);
                return lambda;
            }
        }
    }

    private final Map<Integer, Double> lambdaCache = new HashMap<>();
}
