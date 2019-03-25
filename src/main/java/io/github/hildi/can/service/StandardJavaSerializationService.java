package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.SerializationException;
import io.github.hildi.can.exceptions.FileDoesNotExistException;
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
            throw new FileDoesNotExistException("Failed to serialize data, reason: file " + file.getName() + " doesn't exist.");
        }
        if (!file.canWrite()) {
            throw new NotWritableFileException("Failed to serialize data, reason: " + file.getName() + " is not writable.");
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(user);
        } catch (FileNotFoundException e) {
            System.err.println("Failed to serialize data, reason: file " + file.getName() + " is not found. ");
        } catch (NullPointerException | IOException e) {
            throw new SerializationException("Failed to serialize data. " + file.getName(), e);
        }
    }

    @Override
    public User deserialize(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (User) in.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Failed to deserialize data, reason: file " + file.getName() + " is not found. ");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to deserialize data, reason: class is not found. " + e.getMessage());
        } catch (NullPointerException | IOException e) {
            throw new SerializationException("Failed to deserialize data. " + e.getMessage(), e);
        }
        throw new SerializationException("Failed to deserialize data. File name: " + file.getName());
    }
}
