package by.andd3dfx.math;

public class Interval {

    private double x1;
    private double x2;
    private double h;
    private int n;

    public Interval() {
        this(0, 1, 1);
    }

    public Interval(double x1, double x2, double h) {
        reborn(x1, x2, h);
    }

    public Interval(double x1, double x2, int n) {
        reborn(x1, x2, n);
    }

    public double x1() {
        return x1;
    }

    public double x2() {
        return x2;
    }

    public double h() {
        return h;
    }

    public int n() {
        return n;
    }

    public void reborn(double x1, double x2, double h) {
        assert (x1 < x2 && h > 0 && h <= x2 - x1);

        this.x1 = x1;
        this.x2 = x2;
        this.h = h;
        this.n = (int) Math.floor((x2 - x1) / h);    // если необходимо, n будет на 1 больше
    }

    public void reborn(double x1, double x2, int n) {
        assert (x1 < x2 && n > 0);

        this.x1 = x1;
        this.x2 = x2;
        this.n = n;
        this.h = (x2 - x1) / (double) n;
    }

    public double x(int i) {
        assert (0 <= i && i <= n);

        return x1 + i * h;
    }

    public int i(double x) {
        assert (x1 <= x && x <= x2);

        return (int) ((x - x1) / h);
    }
}
