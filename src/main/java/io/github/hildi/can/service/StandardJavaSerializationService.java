package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.DeserializeException;
import io.github.hildi.can.exceptions.FileDoesNotExistsException;
import io.github.hildi.can.exceptions.NotWritableFileException;
import io.github.hildi.can.model.User;

import java.io.*;

/**
 * Created by Serhii Hildi on 15.03.19.
 */
public class StandardJavaSerializationService implements SerializationService {

    @Override
    public void serialize(User user, File file) {

        if (!file.exists()) {
            throw new FileDoesNotExistsException("Failed to serialize data, reason: file " + file.getName() + " doesn't exist.");
        }
        if (!file.canWrite()){
            throw new NotWritableFileException("Failed to serialize data, reason: " + file.getName() + " doesn't writable.");
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(user);
        } catch (IOException e) {
            System.err.println("Failed to serialize data. " + file.getName());
        }
    }

    @Override
    public User deserialize(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (User) in.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Failed to deserialize data, reason: file " + file.getName() + " don't find. ");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to deserialize data, reason: class don't find. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Failed to deserialize data. " + e.getMessage());
        }
        throw new DeserializeException("Failed to deserialize data. File name: " + file.getName());
    }
}
