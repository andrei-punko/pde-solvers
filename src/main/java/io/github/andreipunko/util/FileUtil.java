package io.github.andreipunko.util;

import io.github.andreipunko.math.matrix.Matrix2D;
import io.github.andreipunko.math.space.Interval;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utilities to write text data to files. All textual output uses UTF-8.
 */
public class FileUtil {

    /**
     * Not used; static helpers only.
     */
    private FileUtil() {
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
     * Save function y(x) on given interval into file
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
        for (var i = 0; i <= interval.n(); i++) {
            var x = interval.x(i);
            var y = func.apply(x);
            sb.append("%s %s\n".formatted(x, y));
        }
        serialize(sb, fileName);
    }

    /**
     * Save function y(x) on given time interval into file for parameterized functions x(t) and y(t)
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
        for (var i = 0; i <= timeInterval.n(); i++) {
            var time = timeInterval.x(i);
            var x = xFunc.apply(time);
            var y = yFunc.apply(time);
            sb.append("%s %s\n".formatted(x, y));
        }
        serialize(sb, fileName);
    }

    /**
     * Save matrix into file
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
        if (rotate) {
            for (int j = 0; j < m.getN(); j++) {
                for (int i = 0; i < m.getM(); i++) {
                    if (i > 0) {
                        sb.append(' ');
                    }
                    sb.append(m.get(i, j));
                }
                sb.append('\n');
            }
        } else {
            for (int i = 0; i < m.getM(); i++) {
                var string = Arrays.stream(m.getRow(i))
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" "));
                sb.append(string).append("\n");
            }
        }
        serialize(sb, fileName);
    }
}
