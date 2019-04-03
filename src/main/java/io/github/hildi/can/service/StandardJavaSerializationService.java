package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.SerializationException;
import io.github.hildi.can.model.User;

import java.io.*;

import static io.github.hildi.can.asserts.AssertFile.*;
import static io.github.hildi.can.asserts.AssertUser.assertParamNotNull;

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
}
