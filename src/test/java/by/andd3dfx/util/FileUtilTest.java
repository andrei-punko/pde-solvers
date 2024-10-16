package by.andd3dfx.util;

import by.andd3dfx.math.Interval;
import by.andd3dfx.math.Matrix;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class FileUtilTest {

    private final String BUILD_PATH = "./build/";
    private final String RESOURCES_PATH = "./src/test/resources/";

    @Test
    void serialize() throws IOException {
        var generatedFileName = BUILD_PATH + "serialize.txt";
        var expectedFileName = RESOURCES_PATH + "serialize.txt";
        StringBuilder sb = new StringBuilder();
        sb.append("one two\n");
        sb.append("three four\n");

        FileUtil.serialize(generatedFileName, sb);

        checkGeneratedFileContent(generatedFileName, expectedFileName);
    }

    @Test
    void saveFuncSimple() throws IOException {
        var generatedFileName = BUILD_PATH + "saveFuncSimple.txt";
        var expectedFileName = RESOURCES_PATH + "saveFuncSimple.txt";
        var interval = new Interval(-1, 9, 10);

        FileUtil.saveFunc(interval, (x) -> x * x, generatedFileName);

        checkGeneratedFileContent(generatedFileName, expectedFileName);
    }

    @Test
    void saveFuncForParameterizedXAndY() throws IOException {
        var generatedFileName = BUILD_PATH + "saveFuncForParameterizedXAndY.txt";
        var expectedFileName = RESOURCES_PATH + "saveFuncForParameterizedXAndY.txt";
        var interval = new Interval(-1, 9, 10);

        FileUtil.saveFunc(interval, (t) -> t * 10, (t) -> 2 * t + 1, generatedFileName);

        checkGeneratedFileContent(generatedFileName, expectedFileName);
    }

    @Test
    void saveMatrixInOriginalView() throws IOException {
        var generatedFileName = BUILD_PATH + "matrix-original.txt";
        var expectedFileName = RESOURCES_PATH + "matrix-original.txt";
        var m = new Matrix(2, 2);
        m.set(0, 0, 5);
        m.set(0, 1, 6);
        m.set(1, 0, 9);
        m.set(1, 1, 1);

        FileUtil.save(m, generatedFileName, false);

        checkGeneratedFileContent(generatedFileName, expectedFileName);
    }

    @Test
    void saveMatrixWithConversionRowsToColumns() throws IOException {
        var generatedFileName = BUILD_PATH + "matrix-not-original.txt";
        var expectedFileName = RESOURCES_PATH + "matrix-not-original.txt";
        var m = new Matrix(2, 2);
        m.set(0, 0, 5);
        m.set(0, 1, 6);
        m.set(1, 0, 9);
        m.set(1, 1, 1);

        FileUtil.save(m, generatedFileName, true);

        checkGeneratedFileContent(generatedFileName, expectedFileName);
    }

    private void checkGeneratedFileContent(String generatedFileName, String expectedOutputFileName) throws IOException {
        Path generatedFilePath = Path.of(generatedFileName);
        String[] generatedFileLines = Files.readString(generatedFilePath).split("\n");

        Path expectedFilePath = Path.of(expectedOutputFileName);
        String[] expectedFileLines = Files.readString(expectedFilePath).split("\n");

        assertThat("Unexpected amount of lines in file " + generatedFilePath,
                generatedFileLines.length, is(expectedFileLines.length));

        for (int i = 0; i < generatedFileLines.length; i++) {
            assertThat("Wrong file content for file " + generatedFilePath,
                    generatedFileLines[i], is(expectedFileLines[i]));
        }
    }
}
