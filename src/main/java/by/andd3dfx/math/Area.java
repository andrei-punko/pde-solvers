package by.andd3dfx.math;

public record Area(Interval x, Interval t) {

    public double tLeft() {
        return t().left();
    }

    public double tRight() {
        return t().right();
    }

    public double xLeft() {
        return x().left();
    }

    public double xRight() {
        return x().right();
    }
}
