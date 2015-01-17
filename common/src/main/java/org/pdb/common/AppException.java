package org.pdb.common;

/**
 * The base exception.
 */
public class AppException extends Exception {

    /**
     * The generated UID.
     */
    private static final long serialVersionUID = -6531708202520128522L;

    /**
     * Creates with the provided message.
     * @param message the error message.
     */
    public AppException(String message) {
        super(message);
    }

    /**
     * Creates with the provided message and cause.
     * @param message the error message.
     * @param cause the error cause.
     */
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
