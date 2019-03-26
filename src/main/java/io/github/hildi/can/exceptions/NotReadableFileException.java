package io.github.hildi.can.exceptions;

/**
 * Created by Serhii Hildi on 26.03.19.
 */
public class NotReadableFileException extends RuntimeException {
    public NotReadableFileException(String message) {
        super(message);
    }
}
