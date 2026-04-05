package io.github.andreipunko.math.space;

import lombok.ToString;

/**
 * Represents a one-dimensional interval with uniform discretization.
 * This class is used to define spatial or temporal domains with a fixed step size
 * or a specified number of subdivisions. It provides methods for coordinate-to-index
 * and index-to-coordinate conversions.
 * <p>
 * <b>Step size {@code h} vs. subdivision count {@code n}:</b>
 * If the interval is built with a step {@code h} (constructor {@link #Interval(double, double, double)}
 * or {@link #reborn(double)}), then {@code n} is computed as
 * {@code (int) ((right - left) / h)} — the integer part of the length-to-step ratio.
 * Grid nodes are {@code x(i) = left + i * h} for {@code i = 0, 1, …, n}. The last node
 * {@code x(n)} satisfies {@code x(n) ≤ right}, but if {@code (right - left) / h} is not an integer,
 * then {@code x(n) < right}: the declared {@code right} boundary is <em>not</em> necessarily a grid point.
 * To obtain a uniform mesh whose last node coincides exactly with {@code right}, use
 * {@link #Interval(double, double, int)} instead (there {@code h = (right - left) / n}).
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
     * The subdivision count {@code n} is {@code (int) ((right - left) / h)}; the endpoint {@code right}
     * may lie beyond the last grid node when the ratio is not integral (see class documentation).
     *
     * @param left  left boundary of the interval
     * @param right right boundary of the interval
     * @param h     step size between points
     * @throws IllegalArgumentException if h &lt;= 0, if (right - left) &lt; h, or if left &gt;= right
     */
    public Interval(double left, double right, double h) {
        reborn(left, right, h);
    }

    /**
     * Creates an interval [left,right] with a specified number of subdivisions {@code n}.
     * The step size is {@code (right - left) / n}, so the last grid node equals {@code right}
     * ({@code x(n) == right}), unlike construction by a fixed step {@code h}.
     *
     * @param left  left boundary of the interval
     * @param right right boundary of the interval
     * @param n     number of subdivisions (grid indices {@code 0 … n}, i.e. {@code n + 1} nodes)
     * @throws IllegalArgumentException if left &gt;= right or n &lt;= 0
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
     * Returns the number of subdivisions along the interval: valid indices are {@code 0 … n}
     * ({@code n + 1} grid nodes). Either supplied to {@link #Interval(double, double, int)}
     * or derived from step {@code h} as described in the class documentation.
     *
     * @return subdivision count {@code n}
     */
    public int n() {
        return n;
    }

    /**
     * Recreates the interval with a new step size while maintaining
     * the same boundaries. {@code n} is recomputed as {@code (int) ((right - left) / h)}
     * (same semantics as {@link #Interval(double, double, double)}; the last node may lie strictly
     * inside {@code right} if the length is not a multiple of {@code h}).
     *
     * @param h new step size
     * @throws IllegalArgumentException if h &lt;= 0 or if (right - left) &lt; h
     */
    public void reborn(double h) {
        reborn(left, right, h);
    }

    private void reborn(double left, double right, double h) {
        if (h <= 0) {
            throw new IllegalArgumentException("step h must be positive, got: " + h);
        }
        if (right - left < h) {
            throw new IllegalArgumentException(
                    "interval length (right - left) must be >= h: left=" + left + ", right=" + right + ", h=" + h);
        }

        this.left = left;
        this.right = right;
        this.h = h;
        this.n = (int) ((right - left) / h);
    }

    /**
     * Recreates the interval with a new subdivision count while maintaining
     * the same boundaries. Same semantics as {@link #Interval(double, double, int)}.
     *
     * @param n new subdivision count (must be positive)
     * @throws IllegalArgumentException if n &lt;= 0
     */
    public void reborn(int n) {
        reborn(left, right, n);
    }

    private void reborn(double left, double right, int n) {
        if (left >= right) {
            throw new IllegalArgumentException("left must be < right: left=" + left + ", right=" + right);
        }
        if (n <= 0) {
            throw new IllegalArgumentException("number of points n must be positive, got: " + n);
        }

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
        if (i < 0 || i > n) {
            throw new IllegalArgumentException("index i out of bounds: " + i + ", valid range [0, " + n + "]");
        }

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
        if (x < left || x > right) {
            throw new IllegalArgumentException("x out of interval [" + left + ", " + right + "]: " + x);
        }

        var res = (int) ((x - left) / h);
        if (res >= n) {
            return n;
        }
        return res;
    }
}
