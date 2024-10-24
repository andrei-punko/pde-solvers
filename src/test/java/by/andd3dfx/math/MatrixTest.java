package by.andd3dfx.math;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatrixTest {

    @Test
    void constructorWithoutParams() {
        var m = new Matrix();

        assertThat(m.getM()).isEqualTo(1);
        assertThat(m.getN()).isEqualTo(1);
    }

    @Test
    void constructorWithOneParam() {
        var m = new Matrix(5);

        assertThat(m.getM()).isEqualTo(1);
        assertThat(m.getN()).isEqualTo(5);
    }

    @Test
    void constructorWithTwoParams() {
        var m = new Matrix(5, 7);

        assertThat(m.getM()).isEqualTo(5);
        assertThat(m.getN()).isEqualTo(7);
    }

    @Test
    void setNGet() {
        var m = new Matrix(3);

        m.set(0, 7);
        m.set(1, 3);
        m.set(2, -3);

        assertThat(m.get(2)).isEqualTo(-3);
        assertThat(m.get(1)).isEqualTo(3);
        assertThat(m.get(0)).isEqualTo(7);
    }

    @Test
    void min() {
        var m = new Matrix(2, 2);
        m.set(0, 0, 5);
        m.set(0, 1, 6);
        m.set(1, 0, 9);
        m.set(1, 1, 1);

        assertThat(m.min()).isEqualTo(1);
    }

    @Test
    void max() {
        var m = new Matrix(2, 2);
        m.set(0, 0, 5);
        m.set(0, 1, 6);
        m.set(1, 0, 9);
        m.set(1, 1, 1);

        assertThat(m.max()).isEqualTo(9);
    }

    @Test
    void swapLines() {
        var m = new Matrix(3, 3);
        m.set(0, 0, 5);
        m.set(0, 1, 6);
        m.set(0, 2, 7);
        m.set(1, 0, 9);
        m.set(1, 1, 1);
        m.set(1, 2, 4);
        m.set(2, 0, 7);
        m.set(2, 1, 0);
        m.set(2, 2, -5);

        m.swapLines(0, 2);

        assertThat(m.get(0, 0)).isEqualTo(7);
        assertThat(m.get(0, 1)).isEqualTo(0);
        assertThat(m.get(0, 2)).isEqualTo(-5);
        assertThat(m.get(1, 0)).isEqualTo(9);
        assertThat(m.get(1, 1)).isEqualTo(1);
        assertThat(m.get(1, 2)).isEqualTo(4);
        assertThat(m.get(2, 0)).isEqualTo(5);
        assertThat(m.get(2, 1)).isEqualTo(6);
        assertThat(m.get(2, 2)).isEqualTo(7);
    }

    @Test
    void swapCols() {
        var m = new Matrix(3, 3);
        m.set(0, 0, 5);
        m.set(1, 0, 9);
        m.set(2, 0, 7);
        m.set(0, 1, 6);
        m.set(1, 1, 1);
        m.set(2, 1, 0);
        m.set(0, 2, 7);
        m.set(1, 2, 4);
        m.set(2, 2, -5);

        m.swapCols(1, 2);

        assertThat(m.get(0, 0)).isEqualTo(5);
        assertThat(m.get(1, 0)).isEqualTo(9);
        assertThat(m.get(2, 0)).isEqualTo(7);
        assertThat(m.get(0, 1)).isEqualTo(7);
        assertThat(m.get(1, 1)).isEqualTo(4);
        assertThat(m.get(2, 1)).isEqualTo(-5);
        assertThat(m.get(0, 2)).isEqualTo(6);
        assertThat(m.get(1, 2)).isEqualTo(1);
        assertThat(m.get(2, 2)).isEqualTo(0);
    }

    @Test
    void setXSetY() {
        var m = new Matrix(2, 4);

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

    @Test
    void fill() {
        var m = new Matrix(2, 3);

        var result = m.fill(4.5);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertThat(m.get(i, j)).isEqualTo(4.5);
            }
        }
        assertThat(result).isEqualTo(m);
    }

    @Test
    void copyArrayIntoDefinitePositionOfData() {
        var m = new Matrix(2, 4);
        m.fill(3);
        m.set(0, 0, 5);
        m.set(0, 2, -5);
        m.set(1, 0, 9);
        m.set(1, 3, 1);

        m.set(1, new double[]{9, 8, 7, 6});

        assertThat(m.get(0, 0)).isEqualTo(5);
        assertThat(m.get(0, 1)).isEqualTo(3);
        assertThat(m.get(0, 2)).isEqualTo(-5);
        assertThat(m.get(0, 3)).isEqualTo(3);
        assertThat(m.get(1, 0)).isEqualTo(9);
        assertThat(m.get(1, 1)).isEqualTo(8);
        assertThat(m.get(1, 2)).isEqualTo(7);
        assertThat(m.get(1, 3)).isEqualTo(6);
    }
}
