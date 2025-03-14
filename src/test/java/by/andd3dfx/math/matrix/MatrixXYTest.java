package by.andd3dfx.math.matrix;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatrixXYTest {

    @Test
    void setXSetY() {
        var m = new MatrixXY(4);

        m.setX(2, 9.5);
        m.setX(3, 3.5);
        m.setY(0, 2);
        m.setY(2, 7);

        assertThat(m.get(0, 0)).isEqualTo(0);
        assertThat(m.get(0, 1)).isEqualTo(0);
        assertThat(m.get(0, 2)).isEqualTo(9.5);
        assertThat(m.get(0, 3)).isEqualTo(3.5);
        assertThat(m.x(0)).isEqualTo(0);
        assertThat(m.x(1)).isEqualTo(0);
        assertThat(m.x(2)).isEqualTo(9.5);
        assertThat(m.x(3)).isEqualTo(3.5);

        assertThat(m.get(1, 0)).isEqualTo(2);
        assertThat(m.get(1, 1)).isEqualTo(0);
        assertThat(m.get(1, 2)).isEqualTo(7);
        assertThat(m.get(1, 3)).isEqualTo(0);
        assertThat(m.y(0)).isEqualTo(2);
        assertThat(m.y(1)).isEqualTo(0);
        assertThat(m.y(2)).isEqualTo(7);
        assertThat(m.y(3)).isEqualTo(0);
    }
}