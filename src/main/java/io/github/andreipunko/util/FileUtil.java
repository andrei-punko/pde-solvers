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
     */
    public static void serialize(StringBuilder sb, String fileName) {
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
     */
    public static void saveFunc(Interval interval, Function<Double, Double> func, String fileName) {
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
     */
    public static void saveFunc(Interval timeInterval, Function<Double, Double> xFunc, Function<Double, Double> yFunc, String fileName) {
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
     */
    public static void save(Matrix2D m, String fileName, boolean rotate) {
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
