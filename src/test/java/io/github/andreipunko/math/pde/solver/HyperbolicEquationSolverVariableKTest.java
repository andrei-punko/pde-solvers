package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.pde.border.DirichletBorderCondition;
import io.github.andreipunko.math.pde.equation.HyperbolicEquation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * <pre>
 * Test for HyperbolicEquationSolver with space-dependent coefficient K(x).
 *
 * Solution of damped wave equation: Utt + 2*Ut = ( K(x)*Ux )x on [0,1], K(x) = 1 + x
 * - U(0,t) = 0 on the left border, U(1,t) = 1 on the right border
 * - zero initial displacement and velocity
 * Damping drives the solution to the steady state of ( K(x)*Ux )x = 0: U(x) = ln(1 + x) / ln(2)
 * (the K(x)*Uxx form would give U(x) = x instead)
 * </pre>
 */
class HyperbolicEquationSolverVariableKTest {

    @Test
    void solve() {
        var rightBorderCondition = new DirichletBorderCondition() {
            @Override
            public double gU(double t) {
                return 1;
            }
        };
        var equation = new HyperbolicEquation(0, 1, 40, new DirichletBorderCondition(), rightBorderCondition) {
            @Override
            public double gK(double x, double t, double U) {
                return 1 + x;
            }

            @Override
            public double gL(double x, double t, double U) {
                return 2;
            }
        };

        var numericU = new HyperbolicEquationSolver().solve(equation, 0.01, 0.01).gUt(40.0);

        for (var i = 0; i < numericU.getN(); i++) {
            var x = numericU.x(i);
            assertThat(numericU.y(i)).isCloseTo(Math.log(1 + x) / Math.log(2), within(1e-4));
        }
    }
}
