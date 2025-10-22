package io.github.andreipunko.math.space;

/**
 * Represents a two-dimensional space-time domain for partial differential equations.
 * This record combines spatial and temporal intervals to define the computational
 * domain where the PDE is solved. It provides methods for coordinate-to-index
 * and index-to-coordinate conversions in both spatial and temporal dimensions.
 *
 * @param x spatial interval defining the domain boundaries and discretization
 * @param t temporal interval defining the time range and discretization
 * @see Interval
 */
public record Area(Interval x, Interval t) {

    /**
     * Returns the left boundary of the temporal domain.
     *
     * @return left boundary of the time interval
     */
    public double tLeft() {
        return t.left();
    }

    /**
     * Returns the right boundary of the temporal domain.
     *
     * @return right boundary of the time interval
     */
    public double tRight() {
        return t.right();
    }

    /**
     * Returns the number of points in the temporal domain.
     *
     * @return number of time points
     */
    public int tn() {
        return t.n();
    }

    /**
     * Returns the time value at the specified temporal index.
     *
     * @param i temporal index (0 &lt;= i &lt;= tn)
     * @return time value at index i
     * @throws IllegalArgumentException if i &lt; 0 or i &gt; tn
     */
    public double tx(int i) {
        return t.x(i);
    }

    /**
     * Returns the temporal index closest to the specified time value.
     *
     * @param time time value (tLeft &lt;= time &lt;= tRight)
     * @return index of the closest time point
     * @throws IllegalArgumentException if time &lt; tLeft or time &gt; tRight
     */
    public int ti(double time) {
        return t.i(time);
    }

    /**
     * Returns the left boundary of the spatial domain.
     *
     * @return left boundary of the spatial interval
     */
    public double xLeft() {
        return x.left();
    }

    /**
     * Returns the right boundary of the spatial domain.
     *
     * @return right boundary of the spatial interval
     */
    public double xRight() {
        return x.right();
    }

    /**
     * Returns the number of points in the spatial domain.
     *
     * @return number of spatial points
     */
    public int xn() {
        return x.n();
    }

    /**
     * Returns the spatial coordinate at the specified spatial index.
     *
     * @param i spatial index (0 &lt;= i &lt;= xn)
     * @return spatial coordinate at index i
     * @throws IllegalArgumentException if i &lt; 0 or i &gt; xn
     */
    public double xx(int i) {
        return x.x(i);
    }

    /**
     * Returns the spatial index closest to the specified coordinate.
     *
     * @param x spatial coordinate (xLeft &lt;= x &lt;= xRight)
     * @return index of the closest spatial point
     * @throws IllegalArgumentException if x &lt; xLeft or x &gt; xRight
     */
    public int xi(double x) {
        return this.x.i(x);
    }
}
