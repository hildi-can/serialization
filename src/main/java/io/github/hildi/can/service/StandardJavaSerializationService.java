package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.DeserializeException;
import io.github.hildi.can.exceptions.FileIsNotExistsException;
import io.github.hildi.can.exceptions.NotWritableFileException;
import io.github.hildi.can.model.User;

import java.io.*;

/**
 * Created by Serhii Hildi on 15.03.19.
 */
public class StandardJavaSerializationService implements SerializationService {

    private User user;

    @Override
    public void serialize(User user, File file) {

        if (!file.exists()) {
            throw new FileIsNotExistsException("We cannot write data at file. File is not exists.");
        }

        if (!file.canWrite()){
            throw new NotWritableFileException("We cannot write data at file. Is not a writable");
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(user);
        } catch (IOException e) {
            System.out.println("We cannot write data at file. " + e.getMessage());
        }
    }

    @Override
    public User deserialize(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (User) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("We cannot find a file. " + e.getMessage());
        } catch (IOException e) {
            System.out.println("We cannot read file data. " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("We cannot find a class. " + e.getMessage());
        }
        throw new DeserializeException("We cannot deserialize this file.");
    }
}
