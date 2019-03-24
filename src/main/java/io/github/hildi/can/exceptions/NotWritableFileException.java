package io.github.hildi.can.exceptions;

/**
 * Created by Serhii Hildi on 24.03.19.
 */
public class NotWritableFileException extends RuntimeException {

    public NotWritableFileException(String message) {
        super(message);
    }
}
