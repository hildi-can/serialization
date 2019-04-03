package io.github.hildi.can.service;

import io.github.hildi.can.model.User;

import java.io.File;

public interface SerializationService {

    /**
     * Reads data from the file, specified by file argument and constructs instance of {@link User} class from it.
     * Details depends on the implementation.
     *
     * @param file - the file, to read user data from
     * @return instance of {@link User} class, never null
     */
    User deserialize(File file);

    /**
     * Stores {@link User} instance's data to the file, described by the file argument.
     *
     * @param user - user to store
     * @param file - file, where data must be stored
     */
    void serialize(User user, File file);

}
