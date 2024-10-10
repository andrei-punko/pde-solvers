package by.andd3dfx.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class FileUtilTest {

    private final String GENERATED_FILE_NAME = "./build/tmp.txt";
    private final String EXPECTED_FILE_NAME = "./src/test/resources/tmp.txt";

    @Test
    void serialize() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("one two\n");
        sb.append("three four\n");

        FileUtil.serialize(GENERATED_FILE_NAME, sb);

        checkGeneratedFileContent(GENERATED_FILE_NAME, EXPECTED_FILE_NAME);
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
