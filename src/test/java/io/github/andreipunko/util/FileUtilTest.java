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
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    /** {@link Path#getParent()} is {@code null} for a single path segment — skip {@code createDirectories}. */
    @Test
    void serialize_acceptsFileNameWithoutParentSegment() throws IOException {
        var name = "fileutil_bare_" + System.nanoTime() + ".txt";
        var path = Path.of(name);
        try {
            FileUtil.serialize(new StringBuilder("bare"), name);
            assertThat(Files.readString(path)).isEqualTo("bare");
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void serialize_throwsIOExceptionWhenPathIsExistingDirectory(@TempDir Path temp) throws IOException {
        var dir = temp.resolve("existing_dir");
        Files.createDirectory(dir);
        assertThrows(IOException.class, () -> FileUtil.serialize(new StringBuilder("x"), dir.toString()));
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
        final var fileName = "matrix-rotated.txt";
        var m = new Matrix2D(2, 3);
        m.setRow(0, new double[]{50, 51, 52});
        m.setRow(1, new double[]{65, 66, 67});

        FileUtil.save(m, BUILD_PATH + fileName, true);

        checkGeneratedFileContent(fileName);
    }

    @Test
    void saveMatrixRotated_singleCell() throws IOException {
        var m = new Matrix2D(1, 1);
        m.set(0, 0, 42);
        var out = Path.of(BUILD_PATH + "matrix-1x1-rotated.txt");
        Files.createDirectories(out.getParent());
        try {
            FileUtil.save(m, out.toString(), true);
            assertThat(Files.readString(out).trim()).isEqualTo("42.0");
        } finally {
            Files.deleteIfExists(out);
        }
    }
}
