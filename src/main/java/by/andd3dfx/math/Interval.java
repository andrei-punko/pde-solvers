package by.andd3dfx.math;

import lombok.ToString;

@ToString
public class Interval {

    private double left;
    private double right;
    private double h;
    private int n;

    public Interval() {
        this(0, 1, 1);
    }

    public Interval(double left, double right, double h) {
        reborn(left, right, h);
    }

    public Interval(double left, double right, int n) {
        reborn(left, right, n);
    }

    public double left() {
        return left;
    }

    public double right() {
        return right;
    }

    public double h() {
        return h;
    }

    public int n() {
        return n;
    }

    private void reborn(double left, double right, double h) {
        assert (right - left >= h && h > 0);

        this.left = left;
        this.right = right;
        this.h = h;
        this.n = (int) ((right - left) / h);
    }

    private void reborn(double left, double right, int n) {
        assert (left < right && n > 0);

        this.left = left;
        this.right = right;
        this.n = n;
        this.h = (right - left) / (double) n;
    }

    public double x(int i) {
        assert (0 <= i && i <= n);

        return left + i * h;
    }

    public int i(double x) {
        assert (left <= x && x <= right);

        var res = (int) ((x - left) / h);
        if (res == n) {
            return n - 1;
        }
        return res;
    }

    public void reborn(double h) {
        reborn(left, right, h);
    }

    public void reborn(int n) {
        reborn(left, right, n);
    }
}
