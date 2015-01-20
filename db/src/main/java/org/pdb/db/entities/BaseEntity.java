package org.pdb.db.entities;

import java.util.Date;

import org.pdb.common.Helper;

/**
 * The base entity for persistence classes.
 */
public abstract class BaseEntity {

    /**
     * The maximum size, in bytes, of access token.
     */
    public static final int ACCESS_TOKEN_MAX_SIZE = 255;

    /**
     * The date of entity creation.
     */
    private Date createdAt;

    /**
     * The date when entity was updated.
     */
    private Date updatedAt;

    /**
     * Default constructor.
     */
    protected BaseEntity() {
        createdAt = new Date();
        updatedAt = createdAt;
    }

    /**
     * Retrieves the 'createdAt' variable.
     * @return the 'createdAt' variable value
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the 'createdAt' variable.
     * @param createdAt the new 'createdAt' variable value to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Retrieves the 'updatedAt' variable.
     * @return the 'updatedAt' variable value
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the 'updatedAt' variable.
     * @param updatedAt the new 'updatedAt' variable value to set
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Override the default toString method for logging purposes.
     * @return JSON string
     */
    @Override
    public String toString() {
        return Helper.toJsonString(this);
    }

}
