package io.github.hildi.can.exceptions;

/**
 * Created by Serhii Hildi on 23.03.19.
 */
public class SerializationException extends RuntimeException {

    public SerializationException(String message) {
        super(message);
    }
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
