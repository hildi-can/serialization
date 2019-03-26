package io.github.hildi.can.exceptions;

/**
 * Created by Serhii Hildi on 26.03.19.
 */
public class InvalidFileException extends RuntimeException {

    public InvalidFileException(String message) {
        super(message);
    }

    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }

}
