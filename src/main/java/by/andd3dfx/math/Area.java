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
     */
    public double tLeft() {
        return t.left();
    }

    /**
     * Right border of time interval
     */
    public double tRight() {
        return t.right();
    }

    /**
     * Steps amount of time interval
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
     * @param time time coordinate
     * @return index of time interval
     */
    public int ti(double time) {
        return t.i(time);
    }

    /**
     * Left border of space interval
     */
    public double xLeft() {
        return x.left();
    }

    /**
     * Right border of space interval
     */
    public double xRight() {
        return x.right();
    }

    /**
     * Steps amount of space interval
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
