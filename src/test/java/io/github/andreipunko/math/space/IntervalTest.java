package io.github.andreipunko.math.space;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntervalTest {

    @Test
    void defaultConstructor() {
        var interval = new Interval();

        assertThat(interval.left()).isEqualTo(0.0);
        assertThat(interval.right()).isEqualTo(1.0);
        assertThat(interval.h()).isEqualTo(1.0);
        assertThat(interval.n()).isEqualTo(1);
    }

    @Test
    void paramsConstructorWithN() {
        var interval = new Interval(2.5, 10.5, 10);

        assertThat(interval.left()).isEqualTo(2.5);
        assertThat(interval.right()).isEqualTo(10.5);
        assertThat(interval.h()).isEqualTo(0.8);
        assertThat(interval.n()).isEqualTo(10);
    }

    @Test
    void paramsConstructorWithNAndWrongParams() {
        assertThrows(AssertionError.class, () -> new Interval(10, 2, 10));
        assertThrows(AssertionError.class, () -> new Interval(2, 10, -2));
    }

    @Test
    void paramsConstructorWithH() {
        var interval = new Interval(2.5, 10.5, 0.1);

        assertThat(interval.left()).isEqualTo(2.5);
        assertThat(interval.right()).isEqualTo(10.5);
        assertThat(interval.h()).isEqualTo(0.1);
        assertThat(interval.n()).isEqualTo(80);
    }

    @Test
    void paramsConstructorWithHAndWrongParams() {
        assertThrows(AssertionError.class, () -> new Interval(10, 2, 0.1));
        assertThrows(AssertionError.class, () -> new Interval(10, 2, 8.5));
        assertThrows(AssertionError.class, () -> new Interval(2, 10, -0.2));
        assertThrows(AssertionError.class, () -> new Interval(2, 10, 8.5));
    }

    @Test
    void rebornWithN() {
        var interval = new Interval(5.0, 10.0, 10);

        interval.reborn(100);

        assertThat(interval.left()).isEqualTo(5.0);
        assertThat(interval.right()).isEqualTo(10.0);
        assertThat(interval.h()).isEqualTo(0.05);
        assertThat(interval.n()).isEqualTo(100);
    }

    @Test
    void rebornWithWrongN() {
        var interval = new Interval(5.0, 10.0, 10);

        assertThrows(AssertionError.class, () -> interval.reborn(-1));
    }

    @Test
    void rebornWithH() {
        var interval = new Interval(5.0, 10.0, 10);

        interval.reborn(0.2);

        assertThat(interval.left()).isEqualTo(5.0);
        assertThat(interval.right()).isEqualTo(10.0);
        assertThat(interval.h()).isEqualTo(0.2);
        assertThat(interval.n()).isEqualTo(25);
    }

    @Test
    void rebornWithWrongH() {
        var interval = new Interval(5.0, 10.0, 10);

        assertThrows(AssertionError.class, () -> interval.reborn(-0.5));
        assertThrows(AssertionError.class, () -> interval.reborn(5.5));
    }

    @Test
    void x() {
        var interval = new Interval(2.0, 12.0, 5);

        assertThat(interval.x(0)).isEqualTo(2.0);
        assertThat(interval.x(2)).isEqualTo(6.0);
        assertThat(interval.x(5)).isEqualTo(12.0);
    }

    @Test
    void xWithWrongParams() {
        var interval = new Interval(2.0, 12.0, 20);

        assertThrows(AssertionError.class, () -> interval.x(-1));
        assertThrows(AssertionError.class, () -> interval.x(21));
    }

    @Test
    void i() {
        var interval = new Interval(2.0, 12.0, 5);

        assertThat(interval.i(2.0)).isEqualTo(0);
        assertThat(interval.i(5.9)).isEqualTo(1);
        assertThat(interval.i(6.1)).isEqualTo(2);
        assertThat(interval.i(11.9)).isEqualTo(4);
        assertThat(interval.i(12.0)).isEqualTo(4);
    }

    @Test
    void iWithWrongParams() {
        var interval = new Interval(2.0, 12.0, 20);

        assertThrows(AssertionError.class, () -> interval.i(1.9));
        assertThrows(AssertionError.class, () -> interval.i(12.3));
    }
}
