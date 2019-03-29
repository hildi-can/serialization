package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.*;
import io.github.hildi.can.model.User;

import java.io.*;

/**
 * Created by Serhii Hildi on 15.03.19.
 */
public class StandardJavaSerializationService implements SerializationService {

    @Override
    public void serialize(User user, File file) {

        assertParamNotNull(user, file);
        assertFileExist(file);
        assertFileWritable(file);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(user);
        } catch (FileNotFoundException e) {
            throw new SerializationException("Failed to serialize data, reason: file " + file.getName() + " is not found. ", e);
        } catch (IOException e) {
            throw new SerializationException("Failed to serialize data. " + file.getName(), e);
        }
    }

    @Override
    public User deserialize(File file) {

        assertFileNotNull(file);
        assertFileReadable(file);

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (User) in.readObject();
        } catch (FileNotFoundException e) {
            throw new SerializationException("Failed to deserialize data, reason: file " + file.getName() + " is not found. ", e);
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Failed to deserialize data, reason: class is not found. " + e.getMessage(), e);
        } catch (IOException e) {
            throw new SerializationException("Failed to deserialize data. " + e.getMessage(), e);
        }
    }

    private static void assertFileExist(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("Failed to serialization, reason: file " + file.getName() + " doesn't exist.");
        }
    }

    private static void assertFileWritable(File file) {
        if (!file.canWrite()) {
            throw new NotWritableFileException("Failed to serialization, reason: " + file.getName() + " is not writable.");
        }
    }

    private static void assertFileReadable(File file) {
        if (!file.canRead()) {
            throw new NotReadableFileException("Failed to deserialize, reason: " + file.getName() + " is not readable.");
        }
    }

    private static void assertParamNotNull(User user, File file) {
        if (user == null) {
            throw new IllegalArgumentException("Failed to serialization, reason: User is null.");
        }
        assertFileNotNull(file);
    }

    private static void assertFileNotNull(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Failed to serialization, reason: File is null.");
        }
    }
}
