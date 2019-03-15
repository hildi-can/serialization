package io.github.hildi.can.service;

import io.github.hildi.can.model.User;

import java.io.*;

/**
 * Created by Serhii Hildi on 15.03.19.
 */
public class StandardJavaSerializationService implements SerializationService {

    @Override
    public User deserialize(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            return (User) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void serialize(User user, File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
            out.writeObject(user);
        } catch (IOException e) {
            System.err.println("Error:" + e.getMessage());
        }
    }
}
