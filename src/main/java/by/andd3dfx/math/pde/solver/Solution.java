package by.andd3dfx.math.pde.solver;

import by.andd3dfx.math.space.Area;
import by.andd3dfx.math.matrix.Matrix2D;
import by.andd3dfx.math.matrix.MatrixXY;
import by.andd3dfx.math.pde.equation.Equation;
import by.andd3dfx.util.FileUtil;

/**
 * Record class representing a numerical solution of a partial differential equation
 * on a defined space-time domain. The solution is stored as a 2D matrix where rows
 * represent time layers and columns represent spatial points.
 *
 * @param equation the partial differential equation that was solved
 * @param area the space-time domain where the solution was found
 * @param solution the numerical solution stored as a 2D matrix
 * @param <E> the type of equation that was solved
 * @see EquationSolver
 * @see Matrix2D
 * @see Area
 */
public record Solution<E extends Equation>(
    /** The partial differential equation that was solved */
    E equation,
    /** The space-time domain where the solution was found */
    Area area,
    /** The numerical solution stored as a 2D matrix */
    Matrix2D solution
) {

    /**
     * Saves solution data U(x,t) for specified time moments to a file.
     * Creates a set of spatial slices of the solution at different time points.
     * Each line in the output file contains spatial coordinates followed by
     * solution values at different time moments.
     *
     * @param fileName name of the file to save the data
     * @param t        array of time moments to save
     * @throws IllegalArgumentException if any time moment is outside the solution domain
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
     * Saves solution data U(x) for a single time moment to a file.
     * Creates a spatial slice of the solution at the specified time.
     *
     * @param fileName name of the file to save the data
     * @param t        time moment to save
     * @throws IllegalArgumentException if the time moment is outside the solution domain
     */
    public void sUt(String fileName, double t) {
        sUt(fileName, new double[]{t});
    }

    /**
     * Saves solution data U(x,t) for specified spatial coordinates to a file.
     * Creates a set of temporal slices of the solution at different spatial points.
     * Each line in the output file contains time coordinates followed by
     * solution values at different spatial points.
     *
     * @param fileName name of the file to save the data
     * @param x        array of spatial coordinates to save
     * @throws IllegalArgumentException if any spatial coordinate is outside the solution domain
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
     * Saves solution data U(t) for a single spatial coordinate to a file.
     * Creates a temporal slice of the solution at the specified spatial point.
     *
     * @param fileName name of the file to save the data
     * @param x        spatial coordinate to save
     * @throws IllegalArgumentException if the spatial coordinate is outside the solution domain
     */
    public void sUx(String fileName, double x) {
        sUx(fileName, new double[]{x});
    }

    /**
     * Retrieves a spatial slice of the solution at a specified time moment.
     * Returns a matrix containing spatial coordinates and corresponding solution values.
     *
     * @param t time moment to get the slice for
     * @return MatrixXY containing the spatial slice of the solution
     * @throws IllegalArgumentException if the time moment is outside the solution domain
     */
    public MatrixXY gUt(double t) {
        return gUt(area.ti(t));
    }

    /**
     * Retrieves a spatial slice of the solution at a specified time layer index.
     * Returns a matrix containing spatial coordinates and corresponding solution values.
     *
     * @param it index of the time layer in the solution matrix (0 &lt;= it &lt; M)
     * @return MatrixXY containing the spatial slice of the solution
     * @throws IllegalArgumentException if the time layer index is out of bounds
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
     * Retrieves a temporal slice of the solution at a specified spatial coordinate.
     * Returns a matrix containing time coordinates and corresponding solution values.
     *
     * @param x spatial coordinate to get the slice for
     * @return MatrixXY containing the temporal slice of the solution
     * @throws IllegalArgumentException if the spatial coordinate is outside the solution domain
     */
    public MatrixXY gUx(double x) {
        return gUx(area.xi(x));
    }

    /**
     * Retrieves a temporal slice of the solution at a specified spatial column index.
     * Returns a matrix containing time coordinates and corresponding solution values.
     *
     * @param ix index of the spatial column in the solution matrix (0 &lt;= ix &lt; N)
     * @return MatrixXY containing the temporal slice of the solution
     * @throws IllegalArgumentException if the spatial column index is out of bounds
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
