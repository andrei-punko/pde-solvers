package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.matrix.Matrix2D;
import io.github.andreipunko.math.matrix.MatrixXY;
import io.github.andreipunko.math.pde.border.DirichletBorderCondition;
import io.github.andreipunko.math.pde.equation.Equation;
import io.github.andreipunko.math.pde.equation.ParabolicEquation;
import io.github.andreipunko.math.space.Area;
import io.github.andreipunko.math.space.Interval;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.github.andreipunko.util.FileComparisonHelper.BUILD_PATH;
import static io.github.andreipunko.util.FileComparisonHelper.checkGeneratedFileContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SolutionTest {

    @Test
    void sUtForArray() throws IOException {
        var fileName = "sUt-array.txt";
        var solution = buildSolution();

        solution.sUt(BUILD_PATH + fileName, new double[]{2.5, 5.0});

        checkGeneratedFileContent(fileName);
    }

    @Test
    void sUtForArrayWithWrongParam() {
        var solution = buildSolution();

        assertThrows(IllegalArgumentException.class, () ->
                solution.sUt(BUILD_PATH + "sUt-arr.txt", new double[]{1.0, 2.5}));
        assertThrows(IllegalArgumentException.class, () ->
                solution.sUt(BUILD_PATH + "sUt-arr.txt", new double[]{2.5, 2.6, 6.5}));
    }

    @Test
    void sUt() throws IOException {
        var fileName = "sUt.txt";
        var solution = buildSolution();

        solution.sUt(BUILD_PATH + fileName, 5.0);

        checkGeneratedFileContent(fileName);
    }

    @Test
    void sUtWithWrongParam() {
        var solution = buildSolution();

        assertThrows(IllegalArgumentException.class, () -> solution.sUt(BUILD_PATH + "sUt.txt", 1.0));
        assertThrows(IllegalArgumentException.class, () -> solution.sUt(BUILD_PATH + "sUt.txt", 6.5));
    }

    @Test
    void sUxForArray() throws IOException {
        var fileName = "sUx-array.txt";
        var solution = buildSolution();

        solution.sUx(BUILD_PATH + fileName, new double[]{11.0, 12.0});

        checkGeneratedFileContent(fileName);
    }

    @Test
    void sUxForArrayWithWrongParam() {
        var solution = buildSolution();

        assertThrows(IllegalArgumentException.class, () ->
                solution.sUx(BUILD_PATH + "sUx-arr.txt", new double[]{9.0, 11.0}));
        assertThrows(IllegalArgumentException.class, () ->
                solution.sUx(BUILD_PATH + "sUx-arr.txt", new double[]{10.0, 12.5, 13.5}));
    }

    @Test
    void sUx() throws IOException {
        var fileName = "sUx.txt";
        var solution = buildSolution();

        solution.sUx(BUILD_PATH + fileName, 12.0);

        checkGeneratedFileContent(fileName);
    }

    @Test
    void sUxWithWrongParam() {
        var solution = buildSolution();

        assertThrows(IllegalArgumentException.class, () -> solution.sUx(BUILD_PATH + "sUx.txt", 9.0));
        assertThrows(IllegalArgumentException.class, () -> solution.sUx(BUILD_PATH + "sUx.txt", 13.5));
    }

    @Test
    void gUtForDouble() {
        var solution = buildSolution();

        var result = solution.gUt(5.0);

        checkUtAssertions(result);
    }

    @Test
    void gUtForInt() {
        var solution = buildSolution();

        var result = solution.gUt(2);

        checkUtAssertions(result);
    }

    @Test
    void gUtForIntWithWrongParam() {
        var solution = buildSolution();

        assertThrows(IllegalArgumentException.class, () -> solution.gUt(-1));
        assertThrows(IllegalArgumentException.class, () -> solution.gUt(solution.solution().getM()));
    }

    @Test
    void gUxForDouble() {
        var solution = buildSolution();

        var result = solution.gUx(10 + 3 / 4.);

        checkUxAssertions(result);
    }

    @Test
    void gUxForInt() {
        var solution = buildSolution();

        var result = solution.gUx(1);

        checkUxAssertions(result);
    }

    @Test
    void gUxForIntWithWrongParam() {
        var solution = buildSolution();

        assertThrows(IllegalArgumentException.class, () -> solution.gUx(-1));
        assertThrows(IllegalArgumentException.class, () -> solution.gUx(solution.solution().getN()));
    }

    /**
     * <pre>
     * Build solution for
     * - space interval [10,13] with n=4 (5 nodes) and time [2,6] with n=3 (4 layers)
     * - matrix (rows = time, cols = space), consistent with {@link Area}:
     *   [[50,80,90,99,100],
     *    [30,70,120,125,130],
     *    [45,56,78,786,800],
     *    [44,55,77,790,805]]
     * </pre>
     */
    private Solution<Equation> buildSolution() {
        var spaceSteps = 4;
        var timeSteps = 3;

        var solutionMatrix = new Matrix2D(timeSteps + 1, spaceSteps + 1);
        solutionMatrix.setRow(0, new double[]{50, 80, 90, 99, 100});
        solutionMatrix.setRow(1, new double[]{30, 70, 120, 125, 130});
        solutionMatrix.setRow(2, new double[]{45, 56, 78, 786, 800});
        solutionMatrix.setRow(3, new double[]{44, 55, 77, 790, 805});

        var equation = new ParabolicEquation(10, 13, 6,
                new DirichletBorderCondition(), new DirichletBorderCondition()) {
        };
        return new Solution<>(equation, new Area(
                new Interval(10, 13, spaceSteps),
                new Interval(2, 6, timeSteps)
        ), solutionMatrix);
    }

    private static void checkUtAssertions(MatrixXY result) {
        assertThat(result.getM()).isEqualTo(2);
        assertThat(result.getN()).isEqualTo(5);
        assertThat(result.x(0)).isEqualTo(10);
        assertThat(result.x(1)).isEqualTo(10 + 3 / 4.);
        assertThat(result.x(2)).isEqualTo(10 + 6 / 4.);
        assertThat(result.x(3)).isEqualTo(10 + 9 / 4.);
        assertThat(result.x(4)).isEqualTo(13);
        assertThat(result.y(0)).isEqualTo(45);
        assertThat(result.y(1)).isEqualTo(56);
        assertThat(result.y(2)).isEqualTo(78);
        assertThat(result.y(3)).isEqualTo(786);
        assertThat(result.y(4)).isEqualTo(800);
    }

    private static void checkUxAssertions(MatrixXY result) {
        assertThat(result.getM()).isEqualTo(2);
        assertThat(result.getN()).isEqualTo(4);
        assertThat(result.x(0)).isEqualTo(2);
        assertThat(result.x(1)).isEqualTo(2 + 4 / 3.);
        assertThat(result.x(2)).isEqualTo(2 + 8 / 3.);
        assertThat(result.x(3)).isEqualTo(6);
        assertThat(result.y(0)).isEqualTo(80);
        assertThat(result.y(1)).isEqualTo(70);
        assertThat(result.y(2)).isEqualTo(56);
        assertThat(result.y(3)).isEqualTo(55);
    }
}
