package io.github.andreipunko.math;

import io.github.andreipunko.math.matrix.Matrix2D;
import io.github.andreipunko.math.pde.border.DirichletBorderCondition;
import io.github.andreipunko.math.pde.equation.ParabolicEquation;
import io.github.andreipunko.math.pde.solver.AbstractEquationSolver;
import io.github.andreipunko.math.pde.solver.ParabolicEquationSolver;
import io.github.andreipunko.math.pde.solver.Solution;
import io.github.andreipunko.math.space.Area;
import io.github.andreipunko.math.space.Interval;
import io.github.andreipunko.util.FileUtil;
import org.junit.jupiter.api.Test;

import static io.github.andreipunko.math.pde.solver.AbstractEquationSolver.KappaNu;
import static io.github.andreipunko.util.FileComparisonHelper.BUILD_PATH;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link IllegalArgumentException} on invalid inputs (null, domain, steps, array lengths).
 */
class InputValidationTest {

    private static ParabolicEquation validEquation() {
        return new ParabolicEquation(0, 1, 1,
                new DirichletBorderCondition(), new DirichletBorderCondition()) {
        };
    }

    @Test
    void equation_rejectsNullBorder() {
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(0, 1, 1, null, new DirichletBorderCondition()));
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(0, 1, 1, new DirichletBorderCondition(), null));
    }

    @Test
    void equation_rejectsBadDomain() {
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(1, 1, 1,
                        new DirichletBorderCondition(), new DirichletBorderCondition()));
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(2, 1, 1,
                        new DirichletBorderCondition(), new DirichletBorderCondition()));
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(0, 1, 0,
                        new DirichletBorderCondition(), new DirichletBorderCondition()));
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(0, 1, -1,
                        new DirichletBorderCondition(), new DirichletBorderCondition()));
    }

    @Test
    void equation_rejectsNonFiniteDomain() {
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(Double.NaN, 1, 1,
                        new DirichletBorderCondition(), new DirichletBorderCondition()));
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(0, Double.NaN, 1,
                        new DirichletBorderCondition(), new DirichletBorderCondition()));
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(0, Double.POSITIVE_INFINITY, 1,
                        new DirichletBorderCondition(), new DirichletBorderCondition()));
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(Double.POSITIVE_INFINITY, 1, 1,
                        new DirichletBorderCondition(), new DirichletBorderCondition()));
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(0, 1, Double.POSITIVE_INFINITY,
                        new DirichletBorderCondition(), new DirichletBorderCondition()));
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquation(0, 1, Double.NaN,
                        new DirichletBorderCondition(), new DirichletBorderCondition()));
    }

    @Test
    void parabolicSolver_rejectsNullEquation() {
        assertThrows(IllegalArgumentException.class, () ->
                new ParabolicEquationSolver().solve(null, 0.1, 0.1));
    }

    @Test
    void parabolicSolver_rejectsBadSteps() {
        var eqn = validEquation();
        var solver = new ParabolicEquationSolver();
        assertThrows(IllegalArgumentException.class, () -> solver.solve(eqn, -0.1, 0.1));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(eqn, 0.1, 0));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(eqn, Double.NaN, 0.1));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(eqn, 0.1, Double.POSITIVE_INFINITY));
    }

    @Test
    void solve3Diagonal_rejectsNullOrMismatchedArrays() {
        double[] a = {0, 1, 1};
        double[] b = {0, 1, 1};
        double[] c = {0, -3, -3};
        double[] f = {0, -3, -4};
        var left = new KappaNu(0, 12);
        var right = new KappaNu(0, 24);

        assertThrows(IllegalArgumentException.class, () ->
                AbstractEquationSolver.solve3DiagonalEquationsSystem(null, b, c, f, left, right));
        assertThrows(IllegalArgumentException.class, () ->
                AbstractEquationSolver.solve3DiagonalEquationsSystem(a, b, c, f, null, right));
        assertThrows(IllegalArgumentException.class, () ->
                AbstractEquationSolver.solve3DiagonalEquationsSystem(a, b, new double[]{0, -3}, f, left, right));
    }

    @Test
    void area_rejectsNullIntervals() {
        var ix = new Interval(0, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> new Area(null, ix));
        assertThrows(IllegalArgumentException.class, () -> new Area(ix, null));
    }

    @Test
    void solution_rejectsNullComponents() {
        var eqn = validEquation();
        var area = new Area(new Interval(0, 1, 1), new Interval(0, 1, 1));
        var matrix = new Matrix2D(2, 2);
        assertThrows(IllegalArgumentException.class, () -> new Solution<>(null, area, matrix));
        assertThrows(IllegalArgumentException.class, () -> new Solution<>(eqn, null, matrix));
        assertThrows(IllegalArgumentException.class, () -> new Solution<>(eqn, area, null));
    }

    @Test
    void solution_sUt_rejectsNullArrayOrFileName() {
        var s = minimalSolution();
        assertThrows(IllegalArgumentException.class, () -> s.sUt(BUILD_PATH + "x.txt", (double[]) null));
        assertThrows(IllegalArgumentException.class, () -> s.sUt(null, new double[]{0}));
        assertThrows(IllegalArgumentException.class, () -> s.sUt(null, 0.0));
    }

    @Test
    void solution_sUx_rejectsNullArrayOrFileName() {
        var s = minimalSolution();
        assertThrows(IllegalArgumentException.class, () -> s.sUx(BUILD_PATH + "x.txt", (double[]) null));
        assertThrows(IllegalArgumentException.class, () -> s.sUx(null, new double[]{0}));
        assertThrows(IllegalArgumentException.class, () -> s.sUx(null, 0.0));
    }

    private static Solution<ParabolicEquation> minimalSolution() {
        var eqn = validEquation();
        var area = new Area(new Interval(0, 1, 2), new Interval(0, 1, 2));
        var matrix = new Matrix2D(3, 3);
        return new Solution<>(eqn, area, matrix);
    }

    @Test
    void fileUtil_rejectsNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> FileUtil.serialize(null, "f.txt"));
        assertThrows(IllegalArgumentException.class, () -> FileUtil.serialize(new StringBuilder("a"), null));
        assertThrows(IllegalArgumentException.class, () -> FileUtil.saveFunc(null, x -> x, "f.txt"));
        assertThrows(IllegalArgumentException.class, () -> FileUtil.saveFunc(new Interval(0, 1, 1), null, "f.txt"));
        assertThrows(IllegalArgumentException.class, () ->
                FileUtil.saveFunc(new Interval(0, 1, 1), (x) -> x, null));
        assertThrows(IllegalArgumentException.class, () ->
                FileUtil.saveFunc(null, (t) -> t, (t) -> t, "f.txt"));
        assertThrows(IllegalArgumentException.class, () ->
                FileUtil.saveFunc(new Interval(0, 1, 1), null, (t) -> t, "f.txt"));
        assertThrows(IllegalArgumentException.class, () ->
                FileUtil.saveFunc(new Interval(0, 1, 1), (t) -> t, null, "f.txt"));
        assertThrows(IllegalArgumentException.class, () ->
                FileUtil.saveFunc(new Interval(0, 1, 1), (t) -> t, (t) -> t, null));
        assertThrows(IllegalArgumentException.class, () -> FileUtil.save(null, "f.txt", false));
        var m = new Matrix2D(1, 1);
        assertThrows(IllegalArgumentException.class, () -> FileUtil.save(m, null, false));
        assertThrows(IllegalArgumentException.class, () -> FileUtil.save(m, null, true));
    }
}
