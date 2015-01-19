package org.pdb.user.rest;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.pdb.common.AppConfigurationException;
import org.pdb.common.Helper;
import org.pdb.db.repo.GroupRepo;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * REST interface for users and groups.
 */
@Path("/api")
public class UserResource {

    @Context
    private ServletContext context;

    /**
     * The Cassandra repo for Group.
     */
    private GroupRepo groupRepo;

    public UserResource() {
        WebApplicationContext applicationContext =
                WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        groupRepo = applicationContext.getBean(GroupRepo.class);
    }

    /**
     * Check initialization.
     * @throws AppConfigurationException if initialization issue encountered
     */
    @PostConstruct
    public void checkInit() {
        Helper.checkNullConfig(groupRepo, "groupRepo");
    }

    @Path("/test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String sayPlainTextHello() {
        return "{\"status\":\"good\"}";
    }

}
