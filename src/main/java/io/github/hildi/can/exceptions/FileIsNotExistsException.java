package io.github.hildi.can.exceptions;

/**
 * Created by Serhii Hildi on 24.03.19.
 */
public class FileIsNotExistsException extends RuntimeException {
    public FileIsNotExistsException(String message) {
        super(message);
    }
}
