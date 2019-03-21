package io.github.hildi.can.service;

import io.github.hildi.can.model.User;

import java.io.*;

/**
 * Created by Serhii Hildi on 15.03.19.
 */
public class StandardJavaSerializationService implements SerializationService {

    @Override
    public void serialize(User user, File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
            out.writeObject(user);
        }
    }

    @Override
    public User deserialize(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (User) in.readObject();
        }
    }
}
