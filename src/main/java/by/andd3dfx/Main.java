package by.andd3dfx;

import by.andd3dfx.math.pde.ParabolicEquation;

public class Main {

    public static void main(String[] args) {
        var depth = 1e-3;       // 1 mm
        var time = 1.0;         // 1 sec

        var equation = new ParabolicEquation(0, depth, time);
        equation.solve(depth / 10., time / 10.);
        equation.sUt("./build/parabolic-solution.txt", time);
    }
}
