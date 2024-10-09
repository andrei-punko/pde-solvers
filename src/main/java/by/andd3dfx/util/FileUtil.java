package by.andd3dfx.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void serialize(String fileName, StringBuilder sb) {
        try (var writer = new FileWriter(fileName)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
