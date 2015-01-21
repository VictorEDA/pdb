package org.pdb.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.pdb.common.rest.ObjectMapperProvider;
import org.pdb.pipes.rest.PipesResource;
import org.pdb.user.rest.HelloResource;
import org.pdb.user.rest.UserResource;

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
        register(UserResource.class);
        register(PipesResource.class);

        // JSON mapper
        register(ObjectMapperProvider.class);

        // Exception mappers
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        register(AppExceptionMapper.class);

    }

}
