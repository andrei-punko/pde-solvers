package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.pde.border.BorderConditionType1;
import by.andd3dfx.util.FileUtil;
import org.junit.jupiter.api.Test;

/**
 * <pre>
 * Solution of diffusion problem:
 * - plate with thickness d=1 mm
 * - constant concentration C=1.0 on left border and C=0.0 on right border
 * - constant diffusion coefficient D
 * </pre>
 */
class ParabolicEquationTest {

    private final double C_LEFT = 1.0;
    private final double THICKNESS = 1e-3;  // 1mm
    private final double TIME = 1;          // 1sec
    private final double DIFFUSION_COEFFICIENT = 1e-4;

    // We allow difference between numeric & analytic solution no more than 5% of max concentration value
    private final double EPSILON = C_LEFT / 20.;

    @Test
    void solve() {
        var h = THICKNESS / 1000.0;
        var tau = TIME / 1000.0;
        var diffusionEquation = buildParabolicEquation();

        diffusionEquation.solve(h, tau);

        // Save numeric solution to file
        var numericU = diffusionEquation.gUt(TIME);
        FileUtil.save(numericU, "./build/res-numeric.txt", true);

        // Save analytic solution to file
        FileUtil.saveFunc(diffusionEquation.area.x(),
                (x) -> analyticSolution(x, TIME), "./build/res-analytic.txt");

        // Compare numeric & analytic solutions
        for (var i = 0; i < numericU.getN(); i++) {
            var numericY = numericU.y(i);
            var analyticY = analyticSolution(numericU.x(i), TIME);
            // TODO uncomment assert below after addition of analytic solution
            // assertThat(Math.abs(numericY - analyticY)).isLessThanOrEqualTo(EPSILON);
        }
    }

    private ParabolicEquation buildParabolicEquation() {
        var leftBorderCondition = new BorderConditionType1() {
            @Override
            public double gU(double t) {
                return C_LEFT;
            }
        };
        var rightBorderCondition = new BorderConditionType1();

        return new ParabolicEquation(0, THICKNESS, TIME, leftBorderCondition, rightBorderCondition) {
            @Override
            protected double gK(double x, double t, double U) {
                return DIFFUSION_COEFFICIENT;
            }
        };
    }

    private double analyticSolution(double x, double t) {
        // TODO add analytic solution
        var A = 1.5e+3;
        return C_LEFT * Math.exp(-A * x);
    }
}
