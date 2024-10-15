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

    public Matrix(int m) {
        this(m, 1);
    }

    public Matrix(int m, int n) {
        assert (m >= 1 && n >= 0);

        this.m = m;
        this.n = n;
        data = new double[m * n];
    }

    public double x(int i) {
        return data(0, i);
    }

    public void setX(int index, double value) {
        data[index] = value;
    }

    public void set(int index, double value) {
        data[index] = value;
    }

    public double get(int i) {
        return data(i, 0);
    }

    public double y(int i) {
        return data(1, i);
    }

    public void setY(int index, double value) {
        data[n + index] = value;
    }

    public void set(int i, int j, double value) {
        data[i * n + j] = value;
    }

    public double data(int i, int j) {
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
        assert (m1 < m && m2 < m && m1 >= 0 && m2 >= 0);

        for (int i = 0; i < n; i++) {
            var tmp = data[m1 * n + i];
            data[m1 * n + i] = data[m2 * n + i];
            data[m2 * n + i] = tmp;
        }
    }

    public void swapCols(int n1, int n2) {
        assert (n1 < n && n2 < n && n1 >= 0 && n2 >= 0);

        for (int i = 0; i < n; i++) {
            var tmp = data[i * n + n1];
            data[i * n + n1] = data[i * n + n2];
            data[i * n + n2] = tmp;
        }
    }
}
