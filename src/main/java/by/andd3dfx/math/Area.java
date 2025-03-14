package by.andd3dfx.math;

/**
 * Space-time area
 *
 * @param x space interval
 * @param t time interval
 */
public record Area(Interval x, Interval t) {

    /**
     * Left border of time interval
     *
     * @return left border value
     */
    public double tLeft() {
        return t.left();
    }

    /**
     * Right border of time interval
     *
     * @return right border value
     */
    public double tRight() {
        return t.right();
    }

    /**
     * Steps amount of time interval
     *
     * @return time steps amount
     */
    public int tn() {
        return t.n();
    }

    /**
     * Time that corresponds to the specified index
     *
     * @param i index of time interval
     * @return time
     */
    public double tx(int i) {
        return t.x(i);
    }

    /**
     * Index that corresponds to the specified time
     *
     * @param time time value
     * @return index of time interval
     */
    public int ti(double time) {
        return t.i(time);
    }

    /**
     * Left border of space interval
     *
     * @return left border value
     */
    public double xLeft() {
        return x.left();
    }

    /**
     * Right border of space interval
     *
     * @return right border value
     */
    public double xRight() {
        return x.right();
    }

    /**
     * Steps amount of space interval
     *
     * @return space steps amount
     */
    public int xn() {
        return x.n();
    }

    /**
     * Coordinate that corresponds to the specified index
     *
     * @param i index of space interval
     * @return space coordinate
     */
    public double xx(int i) {
        return x.x(i);
    }

    /**
     * Index that corresponds to the specified coordinate
     *
     * @param x space coordinate
     * @return index of space interval
     */
    public int xi(double x) {
        return this.x.i(x);
    }
}
