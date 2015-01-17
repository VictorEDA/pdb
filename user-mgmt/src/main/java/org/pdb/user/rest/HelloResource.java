package org.pdb.user.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * X.
 */
@Path("/hello")
public class HelloResource {

    public HelloResource() {
        System.out.println("Creating HelloResource");
    }

    /**
     * Get hello.
     * @return hello
     */
    @GET
    public Response getOrganization() {
        System.out.println("I'm in hello.");
        return Response.ok().entity("Hello World!").build();
    }

}
