package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.pde.border.BorderCondition;
import io.github.andreipunko.math.pde.border.DirichletBorderCondition;
import io.github.andreipunko.math.pde.border.NeumannBorderCondition;
import io.github.andreipunko.math.pde.border.RobinBorderCondition;
import io.github.andreipunko.math.pde.equation.ParabolicEquation;
import io.github.andreipunko.math.pde.solver.AbstractEquationSolver.KappaNu;
import io.github.andreipunko.math.space.Area;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractEquationSolverTest {

    private static final AbstractEquationSolver<ParabolicEquation> PROBE = new AbstractEquationSolver<>() {
        @Override
        public Solution<ParabolicEquation> solve(ParabolicEquation eqn, double h, double tau) {
            throw new UnsupportedOperationException();
        }
    };

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

    /**
     * N = 1: no forward sweep; right boundary denominator 1 - kappa*Alpha[1] = 0 with kappa=1, Alpha[1]=1.
     */
    @Test
    void solve3DiagonalEquationsSystem_rejectsDegenerateRightBoundary() {
        double[] A = {1};
        double[] B = {1};
        double[] C = {1};
        double[] F = {0};

        assertThrows(IllegalArgumentException.class, () ->
                AbstractEquationSolver.solve3DiagonalEquationsSystem(A, B, C, F,
                        new KappaNu(1, 0), new KappaNu(1, 0)));
    }

    @Test
    void solve3DiagonalEquationsSystem_rejectsNonFiniteForwardDenominator() {
        double[] A = {0, 1};
        double[] B = {0, 1};
        double[] C = {0, Double.NaN};
        double[] F = {0, 0};

        assertThrows(IllegalArgumentException.class, () ->
                AbstractEquationSolver.solve3DiagonalEquationsSystem(A, B, C, F,
                        new KappaNu(0, 0), new KappaNu(0, 0)));
    }

    @Test
    void solve3DiagonalEquationsSystem_rejectsInfiniteForwardDenominator() {
        double[] A = {0, 1};
        double[] B = {0, 1};
        double[] C = {0, Double.POSITIVE_INFINITY};
        double[] F = {0, 0};

        assertThrows(IllegalArgumentException.class, () ->
                AbstractEquationSolver.solve3DiagonalEquationsSystem(A, B, C, F,
                        new KappaNu(0, 0), new KappaNu(0, 0)));
    }

    @Test
    void calcKappaNu_rejectsUnknownBorderConditionImplementation() {
        BorderCondition foreign = new BorderCondition() {
        };

        assertThrows(IllegalStateException.class, () -> PROBE.calcKappaNu(foreign, 0.1, 0.0));
    }

    @Test
    void calcKappaNu_dirichletNeumannRobin_produceFiniteKappaNu() {
        var dir = PROBE.calcKappaNu(new DirichletBorderCondition(), 0.1, 0.5);
        assertThat(dir.kappa()).isZero();
        assertThat(dir.nu()).isZero();

        var neu = PROBE.calcKappaNu(new NeumannBorderCondition(), 0.1, 2.0);
        assertThat(neu.kappa()).isEqualTo(1.0);
        assertThat(neu.nu()).isZero();

        var robin = new RobinBorderCondition() {
        };
        var rb = PROBE.calcKappaNu(robin, 0.1, 0.0);
        assertThat(rb.kappa()).isEqualTo(1.0);
        assertThat(rb.nu()).isZero();
    }

    @Test
    void divideThomas_returnsQuotient_whenWellConditioned() {
        assertThat(AbstractEquationSolver.divideThomas(6.0, 3.0, "test")).isEqualTo(2.0);
        assertThat(AbstractEquationSolver.divideThomas(-9.0, 2.0, "test")).isEqualTo(-4.5);
    }

    @Test
    void divideThomas_rejectsNonFiniteDenominator() {
        assertThrows(IllegalArgumentException.class,
                () -> AbstractEquationSolver.divideThomas(1.0, Double.NaN, "d"));
        assertThrows(IllegalArgumentException.class,
                () -> AbstractEquationSolver.divideThomas(1.0, Double.POSITIVE_INFINITY, "d"));
        assertThrows(IllegalArgumentException.class,
                () -> AbstractEquationSolver.divideThomas(1.0, Double.NEGATIVE_INFINITY, "d"));
    }

    @Test
    void divideThomas_rejectsDenominatorTooSmallRelativeToScale() {
        assertThrows(IllegalArgumentException.class,
                () -> AbstractEquationSolver.divideThomas(1e200, 1e-200, "d"));
    }

    /**
     * When {@code scale} becomes NaN (here from a NaN numerator), the "too small" check is skipped and the quotient
     * branch reports a non-finite result.
     */
    @Test
    void divideThomas_rejectsNonFiniteQuotient_fromNanNumerator() {
        var ex = assertThrows(IllegalArgumentException.class,
                () -> AbstractEquationSolver.divideThomas(Double.NaN, 1.0, "q"));
        assertThat(ex.getMessage()).contains("quotient is not finite");
    }

    @Test
    void buildArea_rejectsNullEquation() {
        assertThrows(IllegalArgumentException.class, () -> PROBE.buildArea(null, 0.1, 0.1));
    }

    @Test
    void buildArea_rejectsNonPositiveOrNonFiniteSteps() {
        var eqn = new ParabolicEquation(0, 1, 1,
                new DirichletBorderCondition(), new DirichletBorderCondition()) {
        };
        assertThrows(IllegalArgumentException.class, () -> PROBE.buildArea(eqn, 0, 0.1));
        assertThrows(IllegalArgumentException.class, () -> PROBE.buildArea(eqn, -0.1, 0.1));
        assertThrows(IllegalArgumentException.class, () -> PROBE.buildArea(eqn, Double.NaN, 0.1));
        assertThrows(IllegalArgumentException.class, () -> PROBE.buildArea(eqn, 0.1, 0));
        assertThrows(IllegalArgumentException.class, () -> PROBE.buildArea(eqn, 0.1, Double.POSITIVE_INFINITY));
    }

    @Test
    void buildArea_returnsAreaWithExpectedGrid() {
        var eqn = new ParabolicEquation(0, 1, 2,
                new DirichletBorderCondition(), new DirichletBorderCondition()) {
        };
        Area area = PROBE.buildArea(eqn, 0.25, 0.5);

        assertThat(area.xLeft()).isZero();
        assertThat(area.xRight()).isEqualTo(1.0);
        assertThat(area.tLeft()).isZero();
        assertThat(area.tRight()).isEqualTo(2.0);
        assertThat(area.xn()).isEqualTo(4);
        assertThat(area.tn()).isEqualTo(4);
    }
}
