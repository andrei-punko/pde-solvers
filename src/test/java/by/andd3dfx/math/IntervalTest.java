package by.andd3dfx.math;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
    void paramsConstructorWithH() {
        var interval = new Interval(2.5, 10.5, 0.1);

        assertThat(interval.left()).isEqualTo(2.5);
        assertThat(interval.right()).isEqualTo(10.5);
        assertThat(interval.h()).isEqualTo(0.1);
        assertThat(interval.n()).isEqualTo(80);
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
    void rebornWithH() {
        var interval = new Interval(5.0, 10.0, 10);

        interval.reborn(0.2);

        assertThat(interval.left()).isEqualTo(5.0);
        assertThat(interval.right()).isEqualTo(10.0);
        assertThat(interval.h()).isEqualTo(0.2);
        assertThat(interval.n()).isEqualTo(25);
    }

    @Test
    void x() {
        var interval = new Interval(2.0, 12.0, 5);

        assertThat(interval.x(0)).isEqualTo(2.0);
        assertThat(interval.x(2)).isEqualTo(6.0);
    }

    @Test
    void i() {
        var interval = new Interval(2.0, 12.0, 5);

        assertThat(interval.i(2.0)).isEqualTo(0);
        assertThat(interval.i(5.9)).isEqualTo(1);
        assertThat(interval.i(6.1)).isEqualTo(2);
    }
}
