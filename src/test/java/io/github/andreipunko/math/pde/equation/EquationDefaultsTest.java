package io.github.andreipunko.math.pde.equation;

import io.github.andreipunko.math.pde.border.DirichletBorderCondition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers default coefficient implementations on {@link Equation} (used when subclasses do not override).
 */
class EquationDefaultsTest {

    @Test
    void defaultCoefficients_onParabolicSubclassWithoutOverrides() {
        var eqn = new ParabolicEquation(0, 1, 1,
                new DirichletBorderCondition(), new DirichletBorderCondition()) {
        };

        assertThat(eqn.gU0(0.25)).isZero();
        assertThat(eqn.gM(0.1, 0.2, 3.0)).isZero();
        assertThat(eqn.gK(0.1, 0.2, 3.0)).isEqualTo(1.0);
        assertThat(eqn.gV(0.1, 0.2, 3.0)).isZero();
        assertThat(eqn.gF(0.1, 0.2, 3.0)).isZero();

        assertThat(eqn.getX1()).isZero();
        assertThat(eqn.getX2()).isEqualTo(1.0);
        assertThat(eqn.getT2()).isEqualTo(1.0);
        assertThat(eqn.getLeftBorderCondition()).isNotNull();
        assertThat(eqn.getRightBorderCondition()).isNotNull();
    }
}
