package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.Interval;
import by.andd3dfx.math.pde.border.BorderConditionType1;
import by.andd3dfx.util.FileUtil;
import org.junit.jupiter.api.Test;

class ParabolicEquationTest {

    private final double C_LEFT = 1.0;
    private final double THICKNESS = 1e-3;  // 1mm
    private final double TIME = 1;          // 1sec
    private final double DIFFUSION_COEFFICIENT = 1e-4;

    @Test
    void solve() {
        var h = THICKNESS / 1000.0;
        var tau = TIME / 1000.0;

        var diffusionEquation = new ParabolicEquation(0, THICKNESS, TIME, new BorderConditionType1() {
            @Override
            public double gU(double t) {
                return C_LEFT;
            }
        }, new BorderConditionType1()) {
            @Override
            protected double gK(double x, double t, double U) {
                return DIFFUSION_COEFFICIENT;
            }
        };

        diffusionEquation.solve(h, tau);

        diffusionEquation.sUt("./build/res-numeric.txt", TIME);
        FileUtil.saveFunc(new Interval(0, THICKNESS, h),
                (x) -> analyticSolution(x, TIME), "./build/res-analytic.txt");
    }

    private double analyticSolution(double x, double t) {
        // TODO add analytic solution
        return 0;
    }
}
