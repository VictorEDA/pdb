package org.pdb.common.rest;

/**
 * Json object used for errors.
 */
public class ErrorJson {

    private String status;
    private String message;

    /**
     * Default constructor.
     */
    public ErrorJson() {
        // empty
    }

    /**
     * Constructor that sets error properties.
     * @param status The error status.
     * @param message The error message.
     */
    public ErrorJson(String status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Retrieves the 'status' variable.
     * @return the 'status' variable value
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the 'status' variable.
     * @param status the new 'status' variable value to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves the 'message' variable.
     * @return the 'message' variable value
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the 'message' variable.
     * @param message the new 'message' variable value to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
