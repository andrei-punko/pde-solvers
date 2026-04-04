package io.github.andreipunko.math.pde.border;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RobinBorderConditionDefaultsTest {

    @Test
    void defaultGH_andGTheta_returnZero() {
        var robin = new RobinBorderCondition() {
        };

        assertThat(robin.gH()).isZero();
        assertThat(robin.gTheta(2.5)).isZero();
    }
}
