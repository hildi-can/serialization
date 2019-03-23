package io.github.hildi.can.exceptions;

/**
 * Created by Serhii Hildi on 23.03.19.
 */
public class CannotReadFileException extends RuntimeException {

    public CannotReadFileException(String message) {
        super(message);
    }
}
