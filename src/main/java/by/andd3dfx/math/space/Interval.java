package by.andd3dfx.math.space;

import lombok.ToString;

/**
 * Represents a one-dimensional interval with uniform discretization.
 * This class is used to define spatial or temporal domains with a fixed step size
 * or a specified number of points. It provides methods for coordinate-to-index
 * and index-to-coordinate conversions.
 *
 * @see Area
 */
@ToString
public class Interval {

    private double left;
    private double right;
    private double h;
    private int n;

    /**
     * Creates a default interval [0,1] with a single step.
     * This constructor is provided for convenience and creates the simplest
     * possible interval.
     */
    public Interval() {
        this(0, 1, 1);
    }

    /**
     * Creates an interval [left,right] with a specified step size.
     *
     * @param left  left boundary of the interval
     * @param right right boundary of the interval
     * @param h     step size between points
     * @throws IllegalArgumentException if right &lt;= left or h &lt;= 0
     */
    public Interval(double left, double right, double h) {
        reborn(left, right, h);
    }

    /**
     * Creates an interval [left,right] with a specified number of points.
     * The step size is automatically calculated as (right - left) / n.
     *
     * @param left  left boundary of the interval
     * @param right right boundary of the interval
     * @param n     number of points in the interval
     * @throws IllegalArgumentException if right &lt;= left or n &lt;= 0
     */
    public Interval(double left, double right, int n) {
        reborn(left, right, n);
    }

    /**
     * Returns the left boundary of the interval.
     *
     * @return left boundary value
     */
    public double left() {
        return left;
    }

    /**
     * Returns the right boundary of the interval.
     *
     * @return right boundary value
     */
    public double right() {
        return right;
    }

    /**
     * Returns the step size between points in the interval.
     *
     * @return step size h
     */
    public double h() {
        return h;
    }

    /**
     * Returns the number of points in the interval.
     *
     * @return number of points n
     */
    public int n() {
        return n;
    }

    /**
     * Recreates the interval with a new step size while maintaining
     * the same boundaries. The number of points is automatically adjusted.
     *
     * @param h new step size
     * @throws IllegalArgumentException if h &lt;= 0
     */
    public void reborn(double h) {
        reborn(left, right, h);
    }

    private void reborn(double left, double right, double h) {
        assert (right - left >= h && h > 0);

        this.left = left;
        this.right = right;
        this.h = h;
        this.n = (int) ((right - left) / h);
    }

    /**
     * Recreates the interval with a new number of points while maintaining
     * the same boundaries. The step size is automatically adjusted.
     *
     * @param n new number of points
     * @throws IllegalArgumentException if n &lt;= 0
     */
    public void reborn(int n) {
        reborn(left, right, n);
    }

    private void reborn(double left, double right, int n) {
        assert (left < right && n > 0);

        this.left = left;
        this.right = right;
        this.n = n;
        this.h = (right - left) / (double) n;
    }

    /**
     * Returns the coordinate value at the specified index.
     * The coordinate is calculated as left + i * h.
     *
     * @param i index of the point (0 &lt;= i &lt;= n)
     * @return coordinate value at index i
     * @throws IllegalArgumentException if i &lt; 0 or i &gt; n
     */
    public double x(int i) {
        assert (0 <= i && i <= n);

        return left + i * h;
    }

    /**
     * Returns the index of the point closest to the specified coordinate.
     * If the coordinate is exactly at a grid point, returns its index.
     * If the coordinate is between grid points, returns the index of the
     * leftmost grid point.
     *
     * @param x coordinate value (left &lt;= x &lt;= right)
     * @return index of the closest grid point
     * @throws IllegalArgumentException if x &lt; left or x &gt; right
     */
    public int i(double x) {
        assert (left <= x && x <= right);

        var res = (int) ((x - left) / h);
        if (res == n) {
            return n - 1;
        }
        return res;
    }
}
