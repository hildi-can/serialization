package io.github.hildi.can.exceptions;

/**
 * Created by Serhii Hildi on 24.03.19.
 */
public class FileDoesNotExistException extends RuntimeException {
    public FileDoesNotExistException(String message) {
        super(message);
    }
}
