package io.github.andreipunko.util;

import io.github.andreipunko.math.matrix.Matrix2D;
import io.github.andreipunko.math.pde.solver.Solution;
import io.github.andreipunko.math.space.Interval;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utilities to write text data to files. All textual output uses UTF-8.
 * {@link #saveFunc}, {@link #save(Matrix2D, String, boolean)}, and PDE {@link Solution}
 * methods ({@link Solution#sUt(String, double[])}, {@link Solution#sUx(String, double[])})
 * prefix a short {@code #} header and format numbers with {@link #formatDouble(double)} (data comes from {@link Solution#matrix()}).
 */
public class FileUtil {

    /**
     * Not used; static helpers only.
     */
    private FileUtil() {
    }

    /**
     * Locale-independent numeric text for exports: US decimal point, {@code %.17g} (up to 17 significant digits).
     *
     * @param value number to format
     * @return formatted string suitable for text export files
     */
    public static String formatDouble(double value) {
        return String.format(Locale.US, "%.17g", value);
    }

    private static void appendExportHeader(StringBuilder sb, String title, String columnsLine) {
        sb.append("# pde-solvers: ").append(title).append('\n');
        sb.append("# columns: ").append(columnsLine).append('\n');
    }

    /**
     * Save text data from StringBuilder instance into file using UTF-8 encoding.
     *
     * @param sb       StringBuilder instance
     * @param fileName name of file (parent directories are created if they do not exist)
     * @throws IllegalArgumentException if sb or fileName is null
     * @throws IOException              if an I/O error occurs creating directories or writing the file
     */
    public static void serialize(StringBuilder sb, String fileName) throws IOException {
        if (sb == null) {
            throw new IllegalArgumentException("StringBuilder sb must not be null");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }
        var path = Path.of(fileName);
        var parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(path, sb.toString(), StandardCharsets.UTF_8);
    }

    /**
     * Save function y(x) on given interval into file. Lines starting with {@code #} describe columns;
     * data rows use {@link #formatDouble(double)}.
     *
     * @param interval interval
     * @param func     function y = y(x)
     * @param fileName name of file
     * @throws IllegalArgumentException if interval, func or fileName is null
     * @throws IOException              if an I/O error occurs writing the file
     */
    public static void saveFunc(Interval interval, Function<Double, Double> func, String fileName) throws IOException {
        if (interval == null) {
            throw new IllegalArgumentException("interval must not be null");
        }
        if (func == null) {
            throw new IllegalArgumentException("func must not be null");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }
        var sb = new StringBuilder();
        appendExportHeader(sb, "function samples y(x)", "x y");
        for (var i = 0; i <= interval.n(); i++) {
            var x = interval.x(i);
            var y = func.apply(x);
            sb.append(formatDouble(x)).append(' ').append(formatDouble(y)).append('\n');
        }
        serialize(sb, fileName);
    }

    /**
     * Save function y(x) on given time interval into file for parameterized functions x(t) and y(t).
     * Lines starting with {@code #} describe columns; data rows use {@link #formatDouble(double)}.
     *
     * @param timeInterval time interval
     * @param xFunc        function x = x(t)
     * @param yFunc        function y = y(t)
     * @param fileName     name of file
     * @throws IllegalArgumentException if any argument is null
     * @throws IOException              if an I/O error occurs writing the file
     */
    public static void saveFunc(Interval timeInterval, Function<Double, Double> xFunc, Function<Double, Double> yFunc, String fileName) throws IOException {
        if (timeInterval == null) {
            throw new IllegalArgumentException("timeInterval must not be null");
        }
        if (xFunc == null) {
            throw new IllegalArgumentException("xFunc must not be null");
        }
        if (yFunc == null) {
            throw new IllegalArgumentException("yFunc must not be null");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }
        var sb = new StringBuilder();
        appendExportHeader(sb, "parametric samples x(t), y(t)", "x y");
        for (var i = 0; i <= timeInterval.n(); i++) {
            var time = timeInterval.x(i);
            var x = xFunc.apply(time);
            var y = yFunc.apply(time);
            sb.append(formatDouble(x)).append(' ').append(formatDouble(y)).append('\n');
        }
        serialize(sb, fileName);
    }

    /**
     * Save matrix into file. Lines starting with {@code #} give dimensions and layout; data rows use
     * {@link #formatDouble(double)}.
     *
     * @param m        matrix to save
     * @param fileName name of file
     * @param rotate   flag - rotate matrix or not before saving
     * @throws IllegalArgumentException if m or fileName is null
     * @throws IOException              if an I/O error occurs writing the file
     */
    public static void save(Matrix2D m, String fileName, boolean rotate) throws IOException {
        if (m == null) {
            throw new IllegalArgumentException("matrix m must not be null");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }
        var sb = new StringBuilder();
        var layout = rotate ? "each output row is source column j (j=0..N-1)" : "row-major (matrix rows)";
        sb.append("# pde-solvers: Matrix2D\n");
        sb.append("# source M=").append(m.getM()).append(" N=").append(m.getN())
                .append(" rotate=").append(rotate).append(" (").append(layout).append(")\n");
        sb.append("# columns: ").append(rotate ? m.getM() : m.getN()).append(" values per line\n");
        if (rotate) {
            for (int j = 0; j < m.getN(); j++) {
                for (int i = 0; i < m.getM(); i++) {
                    if (i > 0) {
                        sb.append(' ');
                    }
                    sb.append(formatDouble(m.get(i, j)));
                }
                sb.append('\n');
            }
        } else {
            for (int i = 0; i < m.getM(); i++) {
                var string = Arrays.stream(m.getRow(i))
                        .mapToObj(FileUtil::formatDouble)
                        .collect(Collectors.joining(" "));
                sb.append(string).append("\n");
            }
        }
        serialize(sb, fileName);
    }
}
