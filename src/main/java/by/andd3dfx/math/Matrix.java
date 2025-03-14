package by.andd3dfx.math;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Two-dimensional matrix
 */
@RequiredArgsConstructor
public class Matrix {

    @Getter
    private final int m;
    @Getter
    private final int n;
    private final double[] data;

    /**
     * Create 1d matrix (array) with definite size
     *
     * @param n items amount
     */
    public Matrix(int n) {
        this(1, n);
    }

    /**
     * Create 2d matrix with definite size
     *
     * @param m rows amount
     * @param n columns amount
     */
    public Matrix(int m, int n) {
        assert (m > 0 && n > 0);

        this.m = m;
        this.n = n;
        data = new double[m * n];
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
     * Set X value for definite index
     *
     * @param i     index
     * @param value X value
     */
    public void setX(int i, double value) {
        set(0, i, value);
    }

    public void set(int i, double value) {
        assert (0 <= i && i < data.length);
        data[i] = value;
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
     * Set number into matrix cell
     *
     * @param i     row number
     * @param j     column number
     * @param value number value
     */
    public void set(int i, int j, double value) {
        assert (0 <= i && i < m && 0 <= j && j < n);
        data[i * n + j] = value;
    }

    public void set(int i, double[] arr) {
        assert (0 <= i && i < m && arr.length == n);
        System.arraycopy(arr, 0, data, i * n, arr.length);
    }

    public double[] get(int i) {
        assert (0 <= i && i < m);
        return Arrays.copyOfRange(data, i * n, (i + 1) * n);
    }

    /**
     * Get number stored in matrix cell
     *
     * @param i row number
     * @param j column number
     * @return number value
     */
    public double get(int i, int j) {
        assert (0 <= i && i < m && 0 <= j && j < n);
        return data[i * n + j];
    }

    /**
     * Get min value of stored numbers
     *
     * @return min value
     */
    public double min() {
        return Arrays.stream(data).min().getAsDouble();
    }

    /**
     * Get max value of stored numbers
     *
     * @return max value
     */
    public double max() {
        return Arrays.stream(data).max().getAsDouble();
    }

    /**
     * Fill matrix with numbers of same value
     *
     * @param d number value
     * @return filled matrix
     */
    public Matrix fill(double d) {
        Arrays.fill(data, d);
        return this;
    }

    /**
     * Swap matrix rows
     *
     * @param m1 one row number
     * @param m2 another row number
     */
    public void swapRows(int m1, int m2) {
        assert (0 <= m1 && m1 < m && 0 <= m2 && m2 < m && m1 != m2);

        var buff = new double[n];
        System.arraycopy(data, m1 * n, buff, 0, n);
        System.arraycopy(data, m2 * n, data, m1 * n, n);
        System.arraycopy(buff, 0, data, m2 * n, n);
    }

    /**
     * Swap matrix columns
     *
     * @param n1 one column number
     * @param n2 another column number
     */
    public void swapCols(int n1, int n2) {
        assert (0 <= n1 && n1 < n && 0 <= n2 && n2 < n && n1 != n2);

        for (int i = 0; i < n; i++) {
            var tmp = data[i * n + n1];
            data[i * n + n1] = data[i * n + n2];
            data[i * n + n2] = tmp;
        }
    }
}
