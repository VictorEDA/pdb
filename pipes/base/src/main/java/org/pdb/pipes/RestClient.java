package org.pdb.pipes;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.pdb.common.rest.ObjectMapperProvider;

/**
 * The REST client for sending HTTP requests.
 */
public class RestClient {

    /**
     * The REST client.
     */
    private Client client;

    public RestClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(ObjectMapperProvider.class);
        client = ClientBuilder.newClient(clientConfig);
    }

    /**
     * Retrieves the 'client' variable.
     * @return the 'client' variable value
     */
    public Client getClient() {
        return client;
    }

    /**
     * Sets the 'client' variable.
     * @param client the new 'client' variable value to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
}
