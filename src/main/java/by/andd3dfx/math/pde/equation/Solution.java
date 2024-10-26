package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.Area;
import by.andd3dfx.math.Matrix;
import by.andd3dfx.util.FileUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Solution<E extends Equation> {

    private final E equation;
    private final Matrix solution;

    private Area area() {
        return equation.area;
    }
    
    /**
     * Save data U(x,t_i) for asked time moments [t_i].
     * So in result we get some set of slices for several time moments
     *
     * @param fileName file name
     * @param t        times array
     */
    public void sUt(String fileName, double[] t) {
        for (var t_i : t) {
            assert (area().tLeft() <= t_i && t_i <= area().tRight());
        }

        var sb = new StringBuilder();
        for (var i = 0; i <= area().x().n(); i++) {
            sb.append(area().x().x(i));
            for (var t_i : t) {
                sb.append(" ").append(solution.get(area().t().i(t_i), i));
            }
            sb.append("\n");
        }
        FileUtil.serialize(fileName, sb);
    }

    /**
     * Save data U(x) for asked time moment
     *
     * @param fileName file name
     * @param t        time
     */
    public void sUt(String fileName, double t) {
        sUt(fileName, new double[]{t});
    }

    /**
     * Save data U(x_i, t) for asked space coordinates [x_i].
     * So in result we get some set of slices for several space coordinates
     *
     * @param fileName file name
     * @param x        coordinates array
     */
    public void sUx(String fileName, double[] x) {
        for (var x_i : x) {
            assert (area().xLeft() <= x_i && x_i <= area().xRight());
        }

        var sb = new StringBuilder();
        for (int i = 0; i <= area().t().n(); i++) {
            sb.append(area().t().x(i));
            for (var x_i : x) {
                sb.append(" ").append(solution.get(i, area().x().i(x_i)));
            }
            sb.append("\n");
        }
        FileUtil.serialize(fileName, sb);
    }

    /**
     * Save data U(t) for definite space coordinate
     *
     * @param fileName file name
     * @param x        space coordinate
     */
    public void sUx(String fileName, double x) {
        sUx(fileName, new double[]{x});
    }

    /**
     * Get slice U(x) for definite time
     *
     * @param t time
     * @return U(x) slice
     */
    protected Matrix gUt(double t) {
        return gUt(area().t().i(t));
    }

    /**
     * Get slice U(x) for definite time which corresponds to layer `it` in solution matrix
     *
     * @param it index of time layer in solution matrix
     * @return U(x) slice
     */
    protected Matrix gUt(int it) {
        int M = solution.getM();
        assert (0 <= it && it < M);

        int N = solution.getN();
        var matrix = new Matrix(2, N);
        for (int i = 0; i < N; i++) {
            matrix.setX(i, area().x().x(i));
            matrix.setY(i, solution.get(it, i));
        }
        return matrix;
    }

    /**
     * Get slice U(t) for definite space coordinate
     *
     * @param x space coordinate
     * @return U(t) slice
     */
    protected Matrix gUx(double x) {
        return gUx(area().x().i(x));
    }

    /**
     * Get slice U(t) for definite space coordinate which corresponds to column `ix` in solution matrix
     *
     * @param ix index of space column in solution matrix
     * @return U(t) slice
     */
    protected Matrix gUx(int ix) {
        int N = solution.getN();
        assert (0 <= ix && ix < N);

        int M = solution.getM();
        var matrix = new Matrix(2, M);
        for (int i = 0; i <= M; i++) {
            matrix.setX(i, area().t().x(i));
            matrix.setY(i, solution.get(i, ix));
        }
        return matrix;
    }
}
