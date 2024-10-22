package by.andd3dfx.math;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AreaTest {

    @Test
    void xLeftRightNTLeftRight() {
        var area = new Area(new Interval(3, 8, 10), new Interval(0, 10, 10));

        assertThat(area.xLeft()).isEqualTo(3);
        assertThat(area.xRight()).isEqualTo(8);
        assertThat(area.tLeft()).isEqualTo(0);
        assertThat(area.tRight()).isEqualTo(10);
    }
}
