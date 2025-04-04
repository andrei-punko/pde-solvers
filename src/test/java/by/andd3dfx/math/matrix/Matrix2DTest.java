package by.andd3dfx.math.matrix;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Matrix2DTest {

    @Test
    void constructorWithTwoParams() {
        var m = new Matrix2D(5, 7);

        assertThat(m.getM()).isEqualTo(5);
        assertThat(m.getN()).isEqualTo(7);
    }

    @Test
    void constructorWithInvalidParams() {
        assertThrows(AssertionError.class, () -> new Matrix2D(0, 0));
        assertThrows(AssertionError.class, () -> new Matrix2D(0, 5));
        assertThrows(AssertionError.class, () -> new Matrix2D(-1, 5));
        assertThrows(AssertionError.class, () -> new Matrix2D(5, 0));
        assertThrows(AssertionError.class, () -> new Matrix2D(5, -1));
        assertThrows(AssertionError.class, () -> new Matrix2D(-1, -1));
    }

    @Test
    void setRowNGetRow() {
        var m = new Matrix2D(2, 3);
        m.setRow(0, new double[]{7, 3, -3});
        m.setRow(1, new double[]{78, 79, 90});

        assertThat(m.getRow(0)).isEqualTo(new double[]{7, 3, -3});
        assertThat(m.getRow(1)).isEqualTo(new double[]{78, 79, 90});
    }

    @Test
    void getWithInvalidParams() {
        var m = new Matrix2D(2, 3);

        assertThrows(AssertionError.class, () -> m.get(-1, -1));
        assertThrows(AssertionError.class, () -> m.get(-1, 0));
        assertThrows(AssertionError.class, () -> m.get(-1, 3));
        assertThrows(AssertionError.class, () -> m.get(0, -1));
        assertThrows(AssertionError.class, () -> m.get(0, 3));
        assertThrows(AssertionError.class, () -> m.get(2, -1));
        assertThrows(AssertionError.class, () -> m.get(2, 0));
        assertThrows(AssertionError.class, () -> m.get(2, 3));
    }

    @Test
    void getRowWithInvalidParams() {
        var m = new Matrix2D(2, 3);

        assertThrows(AssertionError.class, () -> m.getRow(-1));
        assertThrows(AssertionError.class, () -> m.getRow(2 * 3));
    }

    @Test
    void setWithInvalidParams() {
        var m = new Matrix2D(2, 3);

        assertThrows(AssertionError.class, () -> m.set(-1, -1, 21));
        assertThrows(AssertionError.class, () -> m.set(-1, 0, 21));
        assertThrows(AssertionError.class, () -> m.set(-1, 3, 21));
        assertThrows(AssertionError.class, () -> m.set(0, -1, 21));
        assertThrows(AssertionError.class, () -> m.set(0, 3, 21));
        assertThrows(AssertionError.class, () -> m.set(2, -1, 21));
        assertThrows(AssertionError.class, () -> m.set(2, 0, 21));
        assertThrows(AssertionError.class, () -> m.set(2, 3, 21));
    }

    @Test
    void setRowWithInvalidParams() {
        var m = new Matrix2D(2, 3);

        assertThrows(AssertionError.class, () -> m.setRow(-1, new double[]{21}));
        assertThrows(AssertionError.class, () -> m.setRow(2 * 3, new double[]{21}));
    }

    @Test
    void min() {
        var m = new Matrix2D(2, 2);
        m.set(0, 0, 5);
        m.set(0, 1, 6);
        m.set(1, 0, 9);
        m.set(1, 1, 1);

        assertThat(m.min()).isEqualTo(1);
    }

    @Test
    void max() {
        var m = new Matrix2D(2, 2);
        m.set(0, 0, 5);
        m.set(0, 1, 6);
        m.set(1, 0, 9);
        m.set(1, 1, 1);

        assertThat(m.max()).isEqualTo(9);
    }

    @Test
    void swapRows() {
        var m = new Matrix2D(3, 3);
        m.set(0, 0, 5);
        m.set(0, 1, 6);
        m.set(0, 2, 7);
        m.set(1, 0, 9);
        m.set(1, 1, 1);
        m.set(1, 2, 4);
        m.set(2, 0, 7);
        m.set(2, 1, 0);
        m.set(2, 2, -5);

        m.swapRows(0, 2);

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
    void swapRowsWithInvalidParams() {
        var m = new Matrix2D(2, 3);

        assertThrows(AssertionError.class, () -> m.swapRows(-1, 0));
        assertThrows(AssertionError.class, () -> m.swapRows(2, 0));
        assertThrows(AssertionError.class, () -> m.swapRows(0, -1));
        assertThrows(AssertionError.class, () -> m.swapRows(0, 2));
        assertThrows(AssertionError.class, () -> m.swapRows(1, 1));
    }

    @Test
    void swapCols() {
        var m = new Matrix2D(3, 3);
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
    void swapColsWithInvalidParams() {
        var m = new Matrix2D(3, 2);

        assertThrows(AssertionError.class, () -> m.swapCols(-1, 0));
        assertThrows(AssertionError.class, () -> m.swapCols(2, 0));
        assertThrows(AssertionError.class, () -> m.swapCols(0, -1));
        assertThrows(AssertionError.class, () -> m.swapCols(0, 2));
        assertThrows(AssertionError.class, () -> m.swapCols(1, 1));
    }

    @Test
    void fill() {
        var m = new Matrix2D(2, 3);

        m.fill(4.5);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertThat(m.get(i, j)).isEqualTo(4.5);
            }
        }
    }

    @Test
    void setNSetRow() {
        var m = new Matrix2D(2, 4);
        m.fill(3);
        m.set(0, 0, 5);
        m.set(0, 2, -5);
        m.set(1, 0, 9);
        m.set(1, 3, 1);

        m.setRow(1, new double[]{9, 8, 7, 6});

        assertThat(m.get(0, 0)).isEqualTo(5);
        assertThat(m.get(0, 1)).isEqualTo(3);
        assertThat(m.get(0, 2)).isEqualTo(-5);
        assertThat(m.get(0, 3)).isEqualTo(3);
        assertThat(m.get(1, 0)).isEqualTo(9);
        assertThat(m.get(1, 1)).isEqualTo(8);
        assertThat(m.get(1, 2)).isEqualTo(7);
        assertThat(m.get(1, 3)).isEqualTo(6);
    }

    @Test
    void setRowWithWrongParams() {
        var m = new Matrix2D(2, 3);

        assertThrows(AssertionError.class, () -> m.setRow(-1, new double[7])); // wrong index (too small), wrong array size
        assertThrows(AssertionError.class, () -> m.setRow(-1, new double[3])); // wrong index (too small)
        assertThrows(AssertionError.class, () -> m.setRow(0, new double[7]));  // wrong array size
        assertDoesNotThrow(() -> m.setRow(0, new double[3]));
        assertDoesNotThrow(() -> m.setRow(1, new double[3]));
        assertThrows(AssertionError.class, () -> m.setRow(2, new double[3]));  // wrong index (too big)
        assertThrows(AssertionError.class, () -> m.setRow(2, new double[2]));  // wrong index (too big), wrong array size
    }
}
