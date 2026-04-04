package io.github.andreipunko.math.matrix;

import lombok.Getter;

import java.util.Arrays;

/**
 * Represents a two-dimensional matrix of real numbers with fixed dimensions.
 * This class provides efficient storage and manipulation of matrix data using
 * a single-dimensional array for better memory locality. It supports basic
 * matrix operations such as element access, row/column swapping, and finding
 * minimum/maximum values.
 * <p>
 * The matrix is stored internally as a single-dimensional array in row-major order,
 * where the element at position (i,j) is stored at index i*n + j in the data array.
 */
public class Matrix2D {

    @Getter
    private final int m;  // number of rows in the matrix
    @Getter
    private final int n;  // number of columns in the matrix
    private final double[] data;  // underlying array storing matrix elements in row-major order

    /**
     * Creates a new matrix with specified dimensions, initialized with zeros.
     *
     * @param m number of rows (must be positive)
     * @param n number of columns (must be positive)
     * @throws IllegalArgumentException if m &lt;= 0 or n &lt;= 0
     */
    public Matrix2D(int m, int n) {
        if (m <= 0 || n <= 0) {
            throw new IllegalArgumentException("m and n must be positive, got m=" + m + ", n=" + n);
        }

        this.m = m;
        this.n = n;
        data = new double[m * n];
    }

    /**
     * Sets a value at the specified matrix position (i,j).
     *
     * @param i     row index (0 &lt;= i &lt; m)
     * @param j     column index (0 &lt;= j &lt; n)
     * @param value value to set
     * @throws IllegalArgumentException if i &lt; 0 or i &gt;= m or j &lt; 0 or j &gt;= n
     */
    public void set(int i, int j, double value) {
        if (i < 0 || i >= m || j < 0 || j >= n) {
            throw new IllegalArgumentException(
                    "indices (i,j) out of bounds: (" + i + "," + j + "), matrix size " + m + "x" + n);
        }
        data[i * n + j] = value;
    }

    /**
     * Sets an entire row of the matrix using the provided array.
     *
     * @param i   row index (0 &lt;= i &lt; m)
     * @param arr array of n values to set in the row
     * @throws IllegalArgumentException if i &lt; 0 or i &gt;= m or arr.length != n
     */
    public void setRow(int i, double[] arr) {
        if (i < 0 || i >= m) {
            throw new IllegalArgumentException("row index i out of bounds: " + i + ", valid [0, " + (m - 1) + "]");
        }
        if (arr == null || arr.length != n) {
            throw new IllegalArgumentException("row array length must be " + n + ", got " + (arr == null ? "null" : arr.length));
        }
        System.arraycopy(arr, 0, data, i * n, arr.length);
    }

    /**
     * Returns a copy of the specified row as an array.
     *
     * @param i row index (0 &lt;= i &lt; m)
     * @return array containing the row elements
     * @throws IllegalArgumentException if i &lt; 0 or i &gt;= m
     */
    public double[] getRow(int i) {
        if (i < 0 || i >= m) {
            throw new IllegalArgumentException("row index i out of bounds: " + i + ", valid [0, " + (m - 1) + "]");
        }
        return Arrays.copyOfRange(data, i * n, (i + 1) * n);
    }

    /**
     * Returns the value at the specified matrix position (i,j).
     *
     * @param i row index (0 &lt;= i &lt; m)
     * @param j column index (0 &lt;= j &lt; n)
     * @return value at position (i,j)
     * @throws IllegalArgumentException if i &lt; 0 or i &gt;= m or j &lt; 0 or j &gt;= n
     */
    public double get(int i, int j) {
        if (i < 0 || i >= m || j < 0 || j >= n) {
            throw new IllegalArgumentException(
                    "indices (i,j) out of bounds: (" + i + "," + j + "), matrix size " + m + "x" + n);
        }
        return data[i * n + j];
    }

    /**
     * Returns the minimum value stored in the matrix.
     *
     * @return minimum value
     */
    public double min() {
        return Arrays.stream(data).min().getAsDouble();
    }

    /**
     * Returns the maximum value stored in the matrix.
     *
     * @return maximum value
     */
    public double max() {
        return Arrays.stream(data).max().getAsDouble();
    }

    /**
     * Fills all elements of the matrix with the specified value.
     *
     * @param d value to fill the matrix with
     */
    public void fill(double d) {
        Arrays.fill(data, d);
    }

    /**
     * Swaps two rows of the matrix.
     *
     * @param m1 index of the first row (0 &lt;= m1 &lt; m)
     * @param m2 index of the second row (0 &lt;= m2 &lt; m)
     * @throws IllegalArgumentException if m1 &lt; 0 or m1 &gt;= m or m2 &lt; 0 or m2 &gt;= m or m1 = m2
     */
    public void swapRows(int m1, int m2) {
        if (m1 < 0 || m1 >= m || m2 < 0 || m2 >= m) {
            throw new IllegalArgumentException(
                    "row indices out of bounds: (" + m1 + "," + m2 + "), valid [0, " + (m - 1) + "]");
        }
        if (m1 == m2) {
            throw new IllegalArgumentException("row indices must differ, both are " + m1);
        }

        var buff = new double[n];
        System.arraycopy(data, m1 * n, buff, 0, n);
        System.arraycopy(data, m2 * n, data, m1 * n, n);
        System.arraycopy(buff, 0, data, m2 * n, n);
    }

    /**
     * Swaps two columns of the matrix.
     *
     * @param n1 index of the first column (0 &lt;= n1 &lt; n)
     * @param n2 index of the second column (0 &lt;= n2 &lt; n)
     * @throws IllegalArgumentException if n1 &lt; 0 or n1 &gt;= n or n2 &lt; 0 or n2 &gt;= n or n1 = n2
     */
    public void swapCols(int n1, int n2) {
        if (n1 < 0 || n1 >= n || n2 < 0 || n2 >= n) {
            throw new IllegalArgumentException(
                    "column indices out of bounds: (" + n1 + "," + n2 + "), valid [0, " + (n - 1) + "]");
        }
        if (n1 == n2) {
            throw new IllegalArgumentException("column indices must differ, both are " + n1);
        }

        for (int i = 0; i < m; i++) {
            var tmp = data[i * n + n1];
            data[i * n + n1] = data[i * n + n2];
            data[i * n + n2] = tmp;
        }
    }
}
