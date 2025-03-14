package by.andd3dfx.math;

/**
 * XY matrix
 */
public class MatrixXY extends Matrix2D {

    /**
     * Create XY matrix
     *
     * @param n matrix size
     */
    public MatrixXY(int n) {
        super(2, n);
    }

    /**
     * Set X value for definite index
     *
     * @param i     index
     * @param value X value
     */
    public void setX(int i, double value) {
        set(0, i, value);
    }

    /**
     * Get X value by index
     *
     * @param i index
     * @return X value
     */
    public double x(int i) {
        return get(0, i);
    }

    /**
     * Set Y value for definite index
     *
     * @param i     index
     * @param value Y value
     */
    public void setY(int i, double value) {
        set(1, i, value);
    }

    /**
     * Get Y value by index
     *
     * @param i index
     * @return Y value
     */
    public double y(int i) {
        return get(1, i);
    }
}
