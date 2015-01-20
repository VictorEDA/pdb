package org.pdb.db.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The authorization parameters for the application to connect to a data pipe.
 */
@Table
public class PipeAppAuthorization extends BaseEntity {

    /**
     * The maximum size, in bytes, of group id.
     */
    public static final int ID_MAX_SIZE = 255;

    /**
     * The group that this user belongs to.
     */
    @NotNull
    @NotEmpty
    @Size(max = ID_MAX_SIZE)
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    /**
     * Retrieves the 'id' variable.
     * @return the 'id' variable value
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the 'id' variable.
     * @param id the new 'id' variable value to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the 'clientId' variable.
     * @return the 'clientId' variable value
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the 'clientId' variable.
     * @param clientId the new 'clientId' variable value to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Retrieves the 'clientSecret' variable.
     * @return the 'clientSecret' variable value
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Sets the 'clientSecret' variable.
     * @param clientSecret the new 'clientSecret' variable value to set
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

}
