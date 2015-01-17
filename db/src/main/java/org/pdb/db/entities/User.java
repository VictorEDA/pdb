package org.pdb.db.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.pdb.common.Helper;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * The user.
 */
@Table
public class User {

    /**
     * The maximum size, in bytes, of user id.
     */
    public static final int USER_ID_MAX_SIZE = 255;

    /**
     * The organization name.
     */
    @NotNull
    @NotEmpty
    @Size(max = USER_ID_MAX_SIZE)
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String userId;

    /**
     * The group that this user belongs to.
     */
    @NotNull
    @NotEmpty
    @PrimaryKeyColumn(ordinal = 1)
    private String groupId;

    /**
     * The access token for the organization.
     * <p>
     * TODO: Encrypt access token in DB.
     */
    @NotNull
    @NotEmpty
    @Size(max = Helper.ACCESS_TOKEN_MAX_SIZE)
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    private String accessToken;

    /**
     * Default constructor.
     */
    public User() {
        // empty
    }

    /**
     * Retrieves the 'userId' variable.
     * @return the 'userId' variable value
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the 'userId' variable.
     * @param userId the new 'userId' variable value to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Retrieves the 'accessToken' variable.
     * @return the 'accessToken' variable value
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets the 'accessToken' variable.
     * @param accessToken the new 'accessToken' variable value to set
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Retrieves the 'groupId' variable.
     * @return the 'groupId' variable value
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Sets the 'groupId' variable.
     * @param groupId the new 'groupId' variable value to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
