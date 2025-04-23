package by.andd3dfx.math.matrix;

/**
 * A specialized matrix for storing pairs of (x,y) coordinates.
 * This class extends Matrix2D to provide a convenient interface for working
 * with coordinate pairs, where the first row stores x-coordinates and the
 * second row stores y-coordinates. The matrix has dimensions 2&times;N, where N
 * is the number of coordinate pairs.
 *
 * @see Matrix2D
 */
public class MatrixXY extends Matrix2D {

    /**
     * Creates a new matrix for storing N coordinate pairs.
     *
     * @param n number of coordinate pairs (must be positive)
     * @throws IllegalArgumentException if n &lt;= 0
     */
    public MatrixXY(int n) {
        super(2, n);
    }

    /**
     * Sets the x-coordinate at the specified index.
     *
     * @param i     index of the coordinate pair (0 &lt;= i &lt; n)
     * @param value x-coordinate value
     * @throws IllegalArgumentException if i &lt; 0 or i &gt;= n
     */
    public void setX(int i, double value) {
        set(0, i, value);
    }

    /**
     * Returns the x-coordinate at the specified index.
     *
     * @param i index of the coordinate pair (0 &lt;= i &lt; n)
     * @return x-coordinate value
     * @throws IllegalArgumentException if i &lt; 0 or i &gt;= n
     */
    public double x(int i) {
        return get(0, i);
    }

    /**
     * Sets the y-coordinate at the specified index.
     *
     * @param i     index of the coordinate pair (0 &lt;= i &lt; n)
     * @param value y-coordinate value
     * @throws IllegalArgumentException if i &lt; 0 or i &gt;= n
     */
    public void setY(int i, double value) {
        set(1, i, value);
    }

    /**
     * Returns the y-coordinate at the specified index.
     *
     * @param i index of the coordinate pair (0 &lt;= i &lt; n)
     * @return y-coordinate value
     * @throws IllegalArgumentException if i &lt; 0 or i &gt;= n
     */
    public double y(int i) {
        return get(1, i);
    }
}
