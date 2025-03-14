package by.andd3dfx.math;

import lombok.ToString;

/**
 * Representation of interval (space/time/etc)
 */
@ToString
public class Interval {

    private double left;
    private double right;
    private double h;
    private int n;

    /**
     * Create interval [0; 1] which contains only one part
     */
    public Interval() {
        this(0, 1, 1);
    }

    /**
     * Create interval [left; right] with provided step size
     *
     * @param left  left border value
     * @param right right border value
     * @param h     step size
     */
    public Interval(double left, double right, double h) {
        reborn(left, right, h);
    }

    /**
     * Create interval [left; right] with provided steps amount
     *
     * @param left  left border value
     * @param right right border value
     * @param n     steps amount
     */
    public Interval(double left, double right, int n) {
        reborn(left, right, n);
    }

    /**
     * Get left border
     *
     * @return left border value
     */
    public double left() {
        return left;
    }

    /**
     * Get right border
     *
     * @return right border value
     */
    public double right() {
        return right;
    }

    /**
     * Get step size
     *
     * @return step size
     */
    public double h() {
        return h;
    }

    /**
     * Get steps amount
     *
     * @return steps amount
     */
    public int n() {
        return n;
    }

    /**
     * Recreate interval with new step size
     *
     * @param h step size
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
     * Recreate interval with new steps amount
     *
     * @param n steps amount
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
     * Get coordinate by index
     *
     * @param i index
     * @return coordinate
     */
    public double x(int i) {
        assert (0 <= i && i <= n);

        return left + i * h;
    }

    /**
     * Get index by coordinate
     *
     * @param x coordinate
     * @return index
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
