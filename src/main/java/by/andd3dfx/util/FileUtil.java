package by.andd3dfx.util;

import by.andd3dfx.math.Interval;
import by.andd3dfx.math.Matrix;

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

    public static void serialize(String fileName, StringBuilder sb) {
        try (var writer = new FileWriter(fileName)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveFunc(Interval spaceInterval, Function<Double, Double> func, String fileName) {
        var sb = new StringBuilder();
        for (var i = 0; i <= spaceInterval.n(); i++) {
            var x = spaceInterval.x(i);
            var y = func.apply(x);
            sb.append("%s %s\n".formatted(x, y));
        }
        serialize(fileName, sb);
    }

    public static void saveFunc(Interval timeInterval, Function<Double, Double> xFunc, Function<Double, Double> yFunc, String fileName) {
        var sb = new StringBuilder();
        for (var i = 0; i <= timeInterval.n(); i++) {
            var time = timeInterval.x(i);
            var x = xFunc.apply(time);
            var y = yFunc.apply(time);
            sb.append("%s %s\n".formatted(x, y));
        }
        serialize(fileName, sb);
    }

    public static void save(Matrix m, String fileName, boolean rotate) {
        var sb = new StringBuilder();
        if (rotate) {
            for (int j = 0; j < m.getN(); j++) {
                var list = new ArrayList<Double>();
                for (int i = 0; i < m.getM(); i++) {
                    list.add(m.get(i, j));
                }
                sb.append(list.stream().map(String::valueOf)
                        .collect(Collectors.joining(" ")) + "\n");
            }
        } else {
            for (int i = 0; i < m.getM(); i++) {
                var string = Arrays.stream(m.get(i))
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" "));
                sb.append(string + "\n");
            }
        }
        serialize(fileName, sb);
    }
}
