package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.pde.solver.AbstractEquationSolver.KappaNu;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractEquationSolverTest {

    /**
     * <pre>
     * Solve next system of equations:
     * y0 + 3*y1 + y2         = 3
     *      y1 + 3*y2 + y3    = 4
     * with border conditions: y0=12, y3=24
     *
     * Solution should be: [y0 y1 y2 y3] = [12.0, -0.875, -6.375, 24.0]
     * </pre>
     */
    @Test
    void solve3DiagonalEquationsSystem() {
        final int AB_VALUE = 1;
        // according to "Тихонов, Самарский - Уравнения математической физики", p.590
        final int C_VALUE = 1 + 2 * AB_VALUE;

        double[] A = new double[]{0, AB_VALUE, AB_VALUE};
        double[] B = new double[]{0, AB_VALUE, AB_VALUE};
        // We put `minus` sign , because coefficients C and F used with minus sign in equation:
        // A[i]*y[i-1] - C[i]*y[i] + B[i]*y[i+1] = -F[i]
        double[] C = new double[]{0, -C_VALUE, -C_VALUE};
        double[] F = new double[]{0, -3, -4};

        var result = AbstractEquationSolver.solve3DiagonalEquationsSystem(A, B, C, F,
                new KappaNu(0, 12), new KappaNu(0, 24));

        assertThat(result).isEqualTo(new double[]{12.0, -0.875, -6.375, 24.0});
    }

    /**
     * Forward sweep denominator C[1] - A[1]*Alpha[1] = 1 - 1*1 = 0 with Alpha[1] = kappa from left boundary.
     */
    @Test
    void solve3DiagonalEquationsSystem_rejectsZeroForwardDenominator() {
        double[] A = {0, 1};
        double[] B = {0, 1};
        double[] C = {0, 1};
        double[] F = {0, 0};

        assertThrows(IllegalArgumentException.class, () ->
                AbstractEquationSolver.solve3DiagonalEquationsSystem(A, B, C, F,
                        new KappaNu(1, 0), new KappaNu(0, 0)));
    }
}
