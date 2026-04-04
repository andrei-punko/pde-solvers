package io.github.andreipunko.util;

import io.github.andreipunko.math.matrix.Matrix2D;
import io.github.andreipunko.math.space.Interval;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.andreipunko.util.FileComparisonHelper.BUILD_PATH;
import static io.github.andreipunko.util.FileComparisonHelper.checkGeneratedFileContent;
import static org.assertj.core.api.Assertions.assertThat;

class FileUtilTest {

    @Test
    void serialize() throws IOException {
        final var fileName = "serialize.txt";
        StringBuilder sb = new StringBuilder();
        sb.append("one two\n");
        sb.append("three four\n");

        FileUtil.serialize(sb, BUILD_PATH + fileName);

        checkGeneratedFileContent(fileName);
    }

    @Test
    void serializeCreatesMissingParentDirectories(@TempDir Path temp) throws IOException {
        var target = temp.resolve("a/b/c/out.txt");
        FileUtil.serialize(new StringBuilder("content"), target.toString());
        assertThat(Files.readString(target)).isEqualTo("content");
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
        var m = new Matrix2D(2, 3);
        m.setRow(0, new double[]{50, 51, 52});
        m.setRow(1, new double[]{65, 66, 67});

        FileUtil.save(m, BUILD_PATH + fileName, false);

        checkGeneratedFileContent(fileName);
    }

    @Test
    void saveMatrixWithConversionRowsToColumns() throws IOException {
        final var fileName = "matrix-not-original.txt";
        var m = new Matrix2D(2, 3);
        m.setRow(0, new double[]{50, 51, 52});
        m.setRow(1, new double[]{65, 66, 67});

        FileUtil.save(m, BUILD_PATH + fileName, true);

        checkGeneratedFileContent(fileName);
    }
}
