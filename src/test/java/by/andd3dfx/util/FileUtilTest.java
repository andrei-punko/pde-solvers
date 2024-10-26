package by.andd3dfx.util;

import by.andd3dfx.math.Interval;
import by.andd3dfx.math.Matrix;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static by.andd3dfx.util.FileComparisonHelper.BUILD_PATH;
import static by.andd3dfx.util.FileComparisonHelper.checkGeneratedFileContent;

class FileUtilTest {

    @Test
    void serialize() throws IOException {
        final var fileName = "serialize.txt";
        StringBuilder sb = new StringBuilder();
        sb.append("one two\n");
        sb.append("three four\n");

        FileUtil.serialize(BUILD_PATH + fileName, sb);

        checkGeneratedFileContent(fileName);
    }

    @Test
    void saveFuncSimple() throws IOException {
        final var fileName = "saveFuncSimple.txt";
        var interval = new Interval(-1, 9, 10);

        FileUtil.saveFunc(interval, (x) -> x * x, BUILD_PATH + fileName);

        checkGeneratedFileContent(fileName);
    }

    @Test
    void saveFuncForParameterizedXAndY() throws IOException {
        final var fileName = "saveFuncForParameterizedXAndY.txt";
        var interval = new Interval(-1, 9, 10);

        FileUtil.saveFunc(interval, (t) -> t * 10, (t) -> 2 * t + 1, BUILD_PATH + fileName);

        checkGeneratedFileContent(fileName);
    }

    @Test
    void saveMatrixInOriginalView() throws IOException {
        final var fileName = "matrix-original.txt";
        var m = new Matrix(2, 3);
        m.set(0, new double[]{50, 51, 52});
        m.set(1, new double[]{65, 66, 67});

        FileUtil.save(m, BUILD_PATH + fileName, false);

        checkGeneratedFileContent(fileName);
    }

    @Test
    void saveMatrixWithConversionRowsToColumns() throws IOException {
        final var fileName = "matrix-not-original.txt";
        var m = new Matrix(2, 3);
        m.set(0, new double[]{50, 51, 52});
        m.set(1, new double[]{65, 66, 67});

        FileUtil.save(m, BUILD_PATH + fileName, true);

        checkGeneratedFileContent(fileName);
    }
}
