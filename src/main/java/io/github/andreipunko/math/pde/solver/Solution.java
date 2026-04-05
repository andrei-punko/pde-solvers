package io.github.andreipunko.math.pde.solver;

import io.github.andreipunko.math.matrix.Matrix2D;
import io.github.andreipunko.math.matrix.MatrixXY;
import io.github.andreipunko.math.pde.equation.Equation;
import io.github.andreipunko.math.space.Area;
import io.github.andreipunko.math.space.Interval;
import io.github.andreipunko.util.FileUtil;

import java.io.IOException;

import static io.github.andreipunko.util.FileUtil.formatDouble;

/**
 * Numerical PDE solution on a space-time domain: {@link #equation()}, {@link #area()}, and
 * grid values {@link #matrix()} ({@link Matrix2D} — rows are time layers, columns are spatial nodes).
 *
 * @param equation the partial differential equation that was solved
 * @param area     the space-time domain where the solution was found
 * @param matrix   grid values of the solution (same layout as above); must not be null
 * @param <E>      the type of equation that was solved
 * @see EquationSolver
 * @see Matrix2D
 * @see Area
 */
public record Solution<E extends Equation>(
        E equation,
        Area area,
        Matrix2D matrix
) {
    /**
     * Validates record components before the instance is created.
     *
     * @throws IllegalArgumentException if equation, area or matrix is null
     */
    public Solution {
        if (equation == null) {
            throw new IllegalArgumentException("equation must not be null");
        }
        if (area == null) {
            throw new IllegalArgumentException("area must not be null");
        }
        if (matrix == null) {
            throw new IllegalArgumentException("matrix must not be null");
        }
    }

    /**
     * Saves solution data U(x,t) for specified time moments to a file.
     * Creates a set of spatial slices of the solution at different time points.
     * The file starts with {@code #}-comment lines describing columns, then numeric rows in US locale
     * ({@link FileUtil#formatDouble(double)}).
     * Each data line contains spatial coordinates followed by
     * solution values at different time moments. All spatial columns of {@link #matrix()} are written
     * (same extent as {@link #gUt(int)} for a fixed time layer).
     * <p>
     * Each {@code t} is mapped to a time-layer index via {@link Area#ti(double)} (see {@link Interval#i(double)}):
     * values are read from the <em>nearest grid time level at or to the left</em> of {@code t}, with no interpolation.
     *
     * @param fileName name of the file to save the data
     * @param t        array of time moments to save
     * @throws IllegalArgumentException if t is null, fileName is null, or any time is outside the solution domain
     * @throws IOException              if an I/O error occurs writing the file
     */
    public void sUt(String fileName, double[] t) throws IOException {
        if (t == null) {
            throw new IllegalArgumentException("time array t must not be null");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }
        for (var t_i : t) {
            if (t_i < area.tLeft() || t_i > area.tRight()) {
                throw new IllegalArgumentException(
                        "time t out of domain [" + area.tLeft() + ", " + area.tRight() + "]: " + t_i);
            }
        }

        var sb = new StringBuilder();
        sb.append("# pde-solvers: spatial slice U(x) at requested times (grid layer: nearest t at or to the left)\n");
        sb.append("# columns: x");
        for (var t_i : t) {
            sb.append(" U(t=").append(formatDouble(t_i)).append(')');
        }
        sb.append('\n');
        for (var i = 0; i < matrix.getN(); i++) {
            sb.append(formatDouble(area.xx(i)));
            for (var t_i : t) {
                sb.append(' ').append(formatDouble(matrix.get(area.ti(t_i), i)));
            }
            sb.append('\n');
        }
        FileUtil.serialize(sb, fileName);
    }

    /**
     * Saves solution data U(x) for a single time moment to a file.
     * Creates a spatial slice of the solution at the specified time.
     * Time is resolved on the grid like {@link #sUt(String, double[])} (via {@link Area#ti(double)}).
     *
     * @param fileName name of the file to save the data
     * @param t        time moment to save
     * @throws IllegalArgumentException if fileName is null or the time moment is outside the solution domain
     * @throws IOException              if an I/O error occurs writing the file
     */
    public void sUt(String fileName, double t) throws IOException {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }
        sUt(fileName, new double[]{t});
    }

    /**
     * Saves solution data U(x,t) for specified spatial coordinates to a file.
     * Creates a set of temporal slices of the solution at different spatial points.
     * The file starts with {@code #}-comment lines describing columns, then numeric rows in US locale
     * ({@link FileUtil#formatDouble(double)}).
     * Each data line contains time coordinates followed by
     * solution values at different spatial points. All time rows of {@link #matrix()} are written
     * (same extent as {@link #gUx(int)} for a fixed spatial column).
     * <p>
     * Each {@code x} is mapped to a spatial column index via {@link Area#xi(double)} (see {@link Interval#i(double)}):
     * values are read from the <em>nearest grid point at or to the left</em> of {@code x}, with no interpolation.
     *
     * @param fileName name of the file to save the data
     * @param x        array of spatial coordinates to save
     * @throws IllegalArgumentException if x is null, fileName is null, or any coordinate is outside the solution domain
     * @throws IOException              if an I/O error occurs writing the file
     */
    public void sUx(String fileName, double[] x) throws IOException {
        if (x == null) {
            throw new IllegalArgumentException("spatial coordinate array x must not be null");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }
        for (var x_i : x) {
            if (x_i < area.xLeft() || x_i > area.xRight()) {
                throw new IllegalArgumentException(
                        "x out of domain [" + area.xLeft() + ", " + area.xRight() + "]: " + x_i);
            }
        }

        var sb = new StringBuilder();
        sb.append("# pde-solvers: temporal slice U(t) at requested positions (grid column: nearest x at or to the left)\n");
        sb.append("# columns: t");
        for (var x_i : x) {
            sb.append(" U(x=").append(formatDouble(x_i)).append(')');
        }
        sb.append('\n');
        for (int i = 0; i < matrix.getM(); i++) {
            sb.append(formatDouble(area.tx(i)));
            for (var x_i : x) {
                sb.append(' ').append(formatDouble(matrix.get(i, area.xi(x_i))));
            }
            sb.append('\n');
        }
        FileUtil.serialize(sb, fileName);
    }

    /**
     * Saves solution data U(t) for a single spatial coordinate to a file.
     * Creates a temporal slice of the solution at the specified spatial point.
     * Position is resolved on the grid like {@link #sUx(String, double[])} (via {@link Area#xi(double)}).
     *
     * @param fileName name of the file to save the data
     * @param x        spatial coordinate to save
     * @throws IllegalArgumentException if fileName is null or the spatial coordinate is outside the solution domain
     * @throws IOException              if an I/O error occurs writing the file
     */
    public void sUx(String fileName, double x) throws IOException {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }
        sUx(fileName, new double[]{x});
    }

    /**
     * Retrieves a spatial slice of the solution at a specified time moment.
     * Returns a matrix containing spatial coordinates and corresponding solution values.
     * Uses {@link Area#ti(double)} to pick the time layer (nearest grid time at or to the left of {@code t}), no interpolation.
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
     * @param it row index in {@link #matrix()} (time layer; 0 &lt;= it &lt; {@code matrix.getM()})
     * @return MatrixXY containing the spatial slice of the solution
     * @throws IllegalArgumentException if the time layer index is out of bounds
     */
    public MatrixXY gUt(int it) {
        int M = matrix.getM();
        if (it < 0 || it >= M) {
            throw new IllegalArgumentException(
                    "time layer index it out of bounds: " + it + ", valid [0, " + (M - 1) + "]");
        }

        int N = matrix.getN();
        var slice = new MatrixXY(N);
        for (int i = 0; i < N; i++) {
            slice.setX(i, area.xx(i));
            slice.setY(i, matrix.get(it, i));
        }
        return slice;
    }

    /**
     * Retrieves a temporal slice of the solution at a specified spatial coordinate.
     * Returns a matrix containing time coordinates and corresponding solution values.
     * Uses {@link Area#xi(double)} to pick the spatial column (nearest grid point at or to the left of {@code x}), no interpolation.
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
     * @param ix column index in {@link #matrix()} (spatial node; 0 &lt;= ix &lt; {@code matrix.getN()})
     * @return MatrixXY containing the temporal slice of the solution
     * @throws IllegalArgumentException if the spatial column index is out of bounds
     */
    public MatrixXY gUx(int ix) {
        int N = matrix.getN();
        if (ix < 0 || ix >= N) {
            throw new IllegalArgumentException(
                    "spatial column index ix out of bounds: " + ix + ", valid [0, " + (N - 1) + "]");
        }

        int M = matrix.getM();
        var slice = new MatrixXY(M);
        for (int i = 0; i < M; i++) {
            slice.setX(i, area.tx(i));
            slice.setY(i, matrix.get(i, ix));
        }
        return slice;
    }
}
