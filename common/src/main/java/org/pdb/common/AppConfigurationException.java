package org.pdb.common;

/**
 * The exception to be used for configuration issues.
 */
public class AppConfigurationException extends RuntimeException {

    /**
     * The generated UID.
     */
    private static final long serialVersionUID = 7811348113788412889L;

    /**
     * Creates with the provided message.
     * @param message the error message.
     */
    public AppConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates with the provided message and cause.
     * @param message the error message.
     * @param cause the error cause.
     */
    public AppConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
