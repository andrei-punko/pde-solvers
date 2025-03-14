package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.space.Area;
import by.andd3dfx.math.matrix.Matrix2D;
import by.andd3dfx.math.matrix.MatrixXY;
import by.andd3dfx.math.pde.equation.Equation;
import by.andd3dfx.util.FileUtil;

/**
 * Solution record class, which represents solution of equation on definite space-time area
 *
 * @param equation equation to solve
 * @param area     space-time area
 * @param solution equation solution
 * @param <E>      particular equation type
 * @see EquationSolver
 */
public record Solution<E extends Equation>(E equation, Area area, Matrix2D solution) {

    /**
     * Save data U(x,t_i) for asked time moments [t_i].
     * So in result we get some set of slices for several time moments
     *
     * @param fileName file name
     * @param t        times array
     */
    public void sUt(String fileName, double[] t) {
        for (var t_i : t) {
            assert (area.tLeft() <= t_i && t_i <= area.tRight());
        }

        var sb = new StringBuilder();
        for (var i = 0; i < area.xn(); i++) {
            sb.append(area.xx(i));
            for (var t_i : t) {
                sb.append(" ").append(solution.get(area.ti(t_i), i));
            }
            sb.append("\n");
        }
        FileUtil.serialize(sb, fileName);
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
     * @param x        space coordinates array
     */
    public void sUx(String fileName, double[] x) {
        for (var x_i : x) {
            assert (area.xLeft() <= x_i && x_i <= area.xRight());
        }

        var sb = new StringBuilder();
        for (int i = 0; i < area.tn(); i++) {
            sb.append(area.tx(i));
            for (var x_i : x) {
                sb.append(" ").append(solution.get(i, area.xi(x_i)));
            }
            sb.append("\n");
        }
        FileUtil.serialize(sb, fileName);
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
    public MatrixXY gUt(double t) {
        return gUt(area.ti(t));
    }

    /**
     * Get slice U(x) for definite time which corresponds to layer `it` in solution matrix
     *
     * @param it index of time layer in solution matrix
     * @return U(x) slice
     */
    public MatrixXY gUt(int it) {
        int M = solution.getM();
        assert (0 <= it && it < M);

        int N = solution.getN();
        var matrix = new MatrixXY(N);
        for (int i = 0; i < N; i++) {
            matrix.setX(i, area.xx(i));
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
    public MatrixXY gUx(double x) {
        return gUx(area.xi(x));
    }

    /**
     * Get slice U(t) for definite space coordinate which corresponds to column `ix` in solution matrix
     *
     * @param ix index of space column in solution matrix
     * @return U(t) slice
     */
    public MatrixXY gUx(int ix) {
        int N = solution.getN();
        assert (0 <= ix && ix < N);

        int M = solution.getM();
        var matrix = new MatrixXY(M);
        for (int i = 0; i < M; i++) {
            matrix.setX(i, area.tx(i));
            matrix.setY(i, solution.get(i, ix));
        }
        return matrix;
    }
}
