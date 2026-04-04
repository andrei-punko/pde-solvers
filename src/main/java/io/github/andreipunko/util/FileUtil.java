package io.github.andreipunko.util;

import io.github.andreipunko.math.matrix.Matrix2D;
import io.github.andreipunko.math.space.Interval;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Util to serialize data into files
 */
public class FileUtil {

    /**
     * Save text data from StringBuilder instance into file
     *
     * @param sb       StringBuilder instance
     * @param fileName name of file
     * @throws IllegalArgumentException if sb or fileName is null
     */
    public static void serialize(StringBuilder sb, String fileName) {
        if (sb == null) {
            throw new IllegalArgumentException("StringBuilder sb must not be null");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }
        try (var writer = new FileWriter(fileName)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Save function y(x) on given interval into file
     *
     * @param interval interval
     * @param func     function y = y(x)
     * @param fileName name of file
     * @throws IllegalArgumentException if interval, func or fileName is null
     */
    public static void saveFunc(Interval interval, Function<Double, Double> func, String fileName) {
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
     */
    public static void saveFunc(Interval timeInterval, Function<Double, Double> xFunc, Function<Double, Double> yFunc, String fileName) {
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
     */
    public static void save(Matrix2D m, String fileName, boolean rotate) {
        if (m == null) {
            throw new IllegalArgumentException("matrix m must not be null");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }
        var sb = new StringBuilder();
        if (rotate) {
            for (int j = 0; j < m.getN(); j++) {
                var list = new ArrayList<Double>();
                for (int i = 0; i < m.getM(); i++) {
                    list.add(m.get(i, j));
                }
                sb.append(list.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(" "))).append("\n");
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
