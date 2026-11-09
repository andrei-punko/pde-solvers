package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.pde.border.DirichletBorderCondition;
import io.github.andreipunko.math.pde.border.NeumannBorderCondition;
import io.github.andreipunko.math.pde.equation.ParabolicEquation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * <pre>
 * Test for ParabolicEquationSolver with Dirichlet border condition on the left side and Neumann on the right side.
 *
 * Solution of heat equation: Ut = Uxx on [0,1]
 * - U(0,t) = 0 on the left border
 * - dU/dx(1,t) = 1 on the right border
 * - zero initial temperature
 * Steady-state solution: U(x) = x
 * </pre>
 *
 * @see <a href="https://github.com/andrei-punko/pde-solvers/issues/17">issue #17</a>
 */
class ParabolicEquationSolverDNTest {

    @Test
    void solve() {
        var equation = new ParabolicEquation(0, 1, 5, new DirichletBorderCondition(), new NeumannBorderCondition() {
            @Override
            public double gdU_dx(double t) {
                return 1;
            }
        });

        var numericU = new ParabolicEquationSolver().solve(equation, 0.01, 0.01).gUt(5.0);

        for (var i = 0; i < numericU.getN(); i++) {
            assertThat(numericU.y(i)).isCloseTo(numericU.x(i), within(1e-4));
        }
    }
}
