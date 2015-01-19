package org.pdb.db.entities;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.pdb.common.Helper;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * The group, which contains one or more users.
 */
@Table
public class Group extends BaseEntity {

    /**
     * The maximum size, in bytes, of group id.
     */
    public static final int GROUP_ID_MAX_SIZE = 255;

    /**
     * The group that this user belongs to.
     */
    @NotNull
    @NotEmpty
    @Size(max = GROUP_ID_MAX_SIZE)
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String groupId;

    /**
     * The user ids belonging to this group.
     */
    private Set<String> userIds;

    /**
     * The access token for the group.
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
    public Group() {
        // empty
    }

    /**
     * Constructor.
     */
    public Group(String groupId, String accessToken) {
        this.groupId = groupId;
        this.accessToken = accessToken;
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

    /**
     * Retrieves the 'userIds' variable.
     * @return the 'userIds' variable value
     */
    public Set<String> getUserIds() {
        if (userIds == null) {
            userIds = new HashSet<String>();
        }
        return userIds;
    }

    /**
     * Sets the 'userIds' variable.
     * @param userIds the new 'userIds' variable value to set
     */
    public void setUserIds(Set<String> userIds) {
        this.userIds = userIds;
    }

}
