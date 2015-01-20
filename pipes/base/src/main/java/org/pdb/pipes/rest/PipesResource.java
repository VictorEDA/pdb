package org.pdb.pipes.rest;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pdb.common.AppConfigurationException;
import org.pdb.common.Helper;
import org.pdb.common.rest.ErrorJson;
import org.pdb.db.entities.PipeAppAuthorization;
import org.pdb.db.repo.PipeAppAuthorizationRepo;
import org.pdb.pipes.PipeService;
import org.springframework.data.cassandra.repository.support.BasicMapId;

/**
 * REST interface for data pipes.
 */
@Path("/api{version:(\\/v1)?}")
public class PipesResource {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LogManager.getLogger(PipesResource.class);

    /**
     * The Cassandra repo.
     */
    @Inject
    private PipeAppAuthorizationRepo pipeAppAuthorizationRepo;

    @Inject
    private List<PipeService> pipeServices;

    /**
     * Map of pipe services by their ids. Created during initialization from pipeServices list.
     */
    private Map<String, PipeService> pipes;

    /**
     * Default constructor.
     */
    public PipesResource() {
        // empty
    }

    /**
     * Check initialization.
     * @throws AppConfigurationException if initialization issue encountered
     */
    @PostConstruct
    public void checkInit() {
        Helper.checkNullConfig(pipeAppAuthorizationRepo, "pipeAppAuthorizationRepo");
        Helper.checkNullConfig(pipeServices, "pipeServices");
        pipes = new HashMap<>();
        for (PipeService pipeService : pipeServices) {
            pipes.put(pipeService.getId(), pipeService);
        }
    }

    /**
     * Save pipe authorization information.
     * @param json The JSON object specifying entity properties.
     * @return 200 and updated entity<br>
     *         201 and created entity<br>
     *         400 if bad request<br>
     *         401 if unauthorized<br>
     *         500 if server error
     */
    @POST
    @Path("/pipes/config")
    @Consumes("application/json")
    @Produces("application/json")
    public Response saveConfig(@Valid PipeAppAuthorization json) {
        final String methodName = "saveConfig";

        Helper.logEntrance(LOGGER, methodName, "json", json);
        Response response = null;

        try {
            PipeAppAuthorization config =
                    pipeAppAuthorizationRepo.findOne(BasicMapId.id().with("id", json.getId()));
            ResponseBuilder responseBuilder = null;
            if (config == null) {
                config = new PipeAppAuthorization();
                config.setId(json.getId());
                responseBuilder = Response.status(Status.CREATED);
            } else {
                responseBuilder = Response.ok();
            }
            config.setClientId(json.getClientId());
            config.setClientSecret(json.getClientSecret());
            config.setUpdatedAt(new Date());
            pipeAppAuthorizationRepo.save(config);
            response = responseBuilder.entity(config).build();
        } catch (Exception e) {
            Helper.logException(LOGGER, methodName, e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorJson("error", e.getMessage())).build();
        }

        return Helper.logExit(LOGGER, methodName, response);
    }

    /**
     * Sync to a pipe.
     * @param pipe The pipe to sync with.
     * @param request The http request.
     * @return 302 and the authorization path<br>
     *         400 if bad request <br>
     *         401 if unauthorized <br>
     *         404 if pipe not found <br>
     *         500 if server error
     */
    @GET
    @Path("/pipes/{pipe}/sync")
    @Produces("application/json")
    public Response syncPipe(@PathParam("pipe") String pipe, @Context HttpServletRequest request) {
        final String methodName = "syncPipe";

        Helper.logEntrance(LOGGER, methodName, "pipe", pipe);
        Response response = null;

        try {
            // Get pipe's authorization
            PipeAppAuthorization config = pipeAppAuthorizationRepo.findOne(BasicMapId.id().with("id", pipe));
            if (config == null) {
                response = Response.status(Status.NOT_FOUND).entity(new ErrorJson("error", "Not found.")).build();
            } else {
                String path = String.format(
                        "https://api.moves-app.com/oauth/v1/authorize?response_type=code&client_id=%s&scope=activity%%20location&redirect_uri=%s",
                        config.getClientId(), "http://localhost:8080/api/pipes/moves/callback");
                // Get the sync endpoint from the proper pipe
                response = Response.temporaryRedirect(URI.create(path)).build();
            }
        } catch (Exception e) {
            Helper.logException(LOGGER, methodName, e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorJson("error", e.getMessage())).build();
        }

        return Helper.logExit(LOGGER, methodName, response);
    }

    @GET
    @Path("/pipes/{pipe}/callback")
    public Response getPipeCallback(@PathParam("pipe") String pipe) {
        return pipeCallback(pipe);
    }

    @POST
    @Path("/pipes/{pipe}/callback")
    public Response postPipeCallback(@PathParam("pipe") String pipe) {
        return pipeCallback(pipe);
    }

    /**
     * Callback for completing pipe sync.
     * @param pipe The pipe to sync with.
     * @return 302 and the authorization path<br>
     *         400 if bad request <br>
     *         401 if unauthorized <br>
     *         404 if pipe not found <br>
     *         500 if server error
     */
    public Response pipeCallback(@PathParam("pipe") String pipe) {
        final String methodName = "pipeCallback";

        Helper.logEntrance(LOGGER, methodName, "pipe", pipe);
        Response response = null;

        response = Response.ok().build();

        return Helper.logExit(LOGGER, methodName, response);
    }

    /**
     * Retrieves the 'pipeAppAuthorizationRepo' variable.
     * @return the 'pipeAppAuthorizationRepo' variable value
     */
    public PipeAppAuthorizationRepo getPipeAppAuthorizationRepo() {
        return pipeAppAuthorizationRepo;
    }

    /**
     * Sets the 'pipeAppAuthorizationRepo' variable.
     * @param pipeAppAuthorizationRepo the new 'pipeAppAuthorizationRepo' variable value to set
     */
    public void setPipeAppAuthorizationRepo(PipeAppAuthorizationRepo pipeAppAuthorizationRepo) {
        this.pipeAppAuthorizationRepo = pipeAppAuthorizationRepo;
    }

}
