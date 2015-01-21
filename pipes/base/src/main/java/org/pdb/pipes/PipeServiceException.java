package org.pdb.pipes;

import org.pdb.common.AppException;


/**
 * The base exception for the pipe services.
 */
public class PipeServiceException extends AppException {

    /**
     * The generated UID.
     */
    private static final long serialVersionUID = 6063579584361988400L;

    /**
     * Creates with the provided message.
     * @param message the error message.
     */
    public PipeServiceException(String message) {
        super(message);
    }

    /**
     * Creates with the provided message and cause.
     * @param message the error message.
     * @param cause the error cause.
     */
    public PipeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
