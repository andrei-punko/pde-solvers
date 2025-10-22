package io.github.andreipunko.math.space;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AreaTest {

    @Test
    void xLeft() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 10));

        assertThat(area.xLeft()).isEqualTo(3);
    }

    @Test
    void xRight() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 10));

        assertThat(area.xRight()).isEqualTo(8);
    }

    @Test
    void tLeft() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 10));

        assertThat(area.tLeft()).isEqualTo(0);
    }

    @Test
    void tRight() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 10));

        assertThat(area.tRight()).isEqualTo(10);
    }

    @Test
    void xn() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 20));

        assertThat(area.xn()).isEqualTo(10);
    }

    @Test
    void xx() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 20));

        assertThat(area.xx(3)).isEqualTo(4.5d);
    }

    @Test
    void xi() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 20));

        assertThat(area.xi(5.5)).isEqualTo(5);
    }

    @Test
    void tn() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 20));

        assertThat(area.tn()).isEqualTo(20);
    }

    @Test
    void tx() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 20));

        assertThat(area.tx(3)).isEqualTo(1.5d);
    }

    @Test
    void ti() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 20));

        assertThat(area.ti(5.5)).isEqualTo(11);
    }
}
