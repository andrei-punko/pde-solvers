package by.andd3dfx.math;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public class Matrix {

    @Getter
    private final int m;
    @Getter
    private final int n;
    private final double[] data;

    public Matrix() {
        this(1);
    }

    public Matrix(int n) {
        this(1, n);
    }

    public Matrix(int m, int n) {
        assert (m > 0 && n > 0);

        this.m = m;
        this.n = n;
        data = new double[m * n];
    }

    public double x(int i) {
        return get(0, i);
    }

    public void setX(int i, double value) {
        set(0, i, value);
    }

    public void set(int i, double value) {
        assert (i >= 0 && i < data.length);
        data[i] = value;
    }

    public double get(int i) {
        return get(0, i);
    }

    public double y(int i) {
        return get(1, i);
    }

    public void setY(int i, double value) {
        set(1, i, value);
    }

    public void set(int i, int j, double value) {
        assert (i >= 0 && i < m && j >= 0 && j < n);
        data[i * n + j] = value;
    }

    public void set(int i, double[] arr) {
        assert (i >= 0 && i < m && n == arr.length);
        System.arraycopy(arr, 0, data, i * n, arr.length);
    }

    public double get(int i, int j) {
        assert (i >= 0 && i < m && j >= 0 && j < n);
        return data[i * n + j];
    }

    public double min() {
        return Arrays.stream(data).min().getAsDouble();
    }

    public double max() {
        return Arrays.stream(data).max().getAsDouble();
    }

    public Matrix fill(double d) {
        Arrays.fill(data, d);
        return this;
    }

    public void swapLines(int m1, int m2) {
        assert (0 <= m1 && m1 < m && 0 <= m2 && m2 < m && m1 != m2);

        var buff = new double[n];
        System.arraycopy(data, m1 * n, buff, 0, n);
        System.arraycopy(data, m2 * n, data, m1 * n, n);
        System.arraycopy(buff, 0, data, m2 * n, n);
    }

    public void swapCols(int n1, int n2) {
        assert (0 <= n1 && n1 < n && 0 <= n2 && n2 < n && n1 != n2);

        for (int i = 0; i < n; i++) {
            var tmp = data[i * n + n1];
            data[i * n + n1] = data[i * n + n2];
            data[i * n + n2] = tmp;
        }
    }
}
