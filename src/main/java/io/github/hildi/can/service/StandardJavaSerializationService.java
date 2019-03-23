package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.CannotReadFileException;
import io.github.hildi.can.model.User;

import java.io.*;

/**
 * Created by Serhii Hildi on 15.03.19.
 */
public class StandardJavaSerializationService implements SerializationService {

    @Override
    public void serialize(User user, File file) {

        if (!(file.exists() || file.canWrite())) { return; }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User deserialize(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (User) in.readObject();
        } catch (EOFException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new CannotReadFileException("We cannot read bytes of array at this files.");
    }
}
