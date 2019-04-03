package io.github.hildi.can.asserts;

import io.github.hildi.can.exceptions.InvalidFileException;
import io.github.hildi.can.exceptions.NotReadableFileException;
import io.github.hildi.can.exceptions.NotWritableFileException;

import java.io.File;

/**
 * Created by Serhii Hildi on 04.04.19.
 */
public class AssertFile {
    public static void assertFileExist(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("Failed to serialize, reason: file " + file.getName() + " doesn't exist.");
        }
    }

    public static void assertFileWritable(File file) {
        if (!file.canWrite()) {
            throw new NotWritableFileException("Failed to serialize, reason: " + file.getName() + " is not writable.");
        }
    }

    public static void assertFileReadable(File file) {
        if (!file.canRead()) {
            throw new NotReadableFileException("Failed to deserialize, reason: " + file.getName() + " is not readable or doesn't exist.");
        }
    }

    public static void assertFileNotNull(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Failed to serialize, reason: File is null.");
        }
    }

    public static void assertFileExtensionIsCorrect(File file) {
        String name = file.getName();
        if (!name.endsWith(".properties")){
            throw new InvalidFileException("Failed to serialize data, reason: invalid file extension " + file.getName());
        }
    }
}
