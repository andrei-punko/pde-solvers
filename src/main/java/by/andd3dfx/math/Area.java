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
        return t().left();
    }

    /**
     * Right border of time interval
     */
    public double tRight() {
        return t().right();
    }

    /**
     * Left border of space interval
     */
    public double xLeft() {
        return x().left();
    }

    /**
     * Right border of space interval
     */
    public double xRight() {
        return x().right();
    }
}
