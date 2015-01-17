package org.pdb.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.pdb.user.rest.HelloResource;

/**
 * Configuration for REST resources.
 */
public class RestConfig extends ResourceConfig {

    /**
     * Constructor. Register JAX-RS application components.
     */
    public RestConfig() {
        // REST endpoints
        register(HelloResource.class);

    }

}
