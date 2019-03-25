package io.github.hildi.can.exceptions;

/**
 * Created by Serhii Hildi on 24.03.19.
 */
public class FileDoesNotExistsException extends RuntimeException {
    public FileDoesNotExistsException(String message) {
        super(message);
    }
}
