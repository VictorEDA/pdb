package org.pdb.pipes.moves;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pdb.common.AppConfigurationException;
import org.pdb.common.Helper;
import org.pdb.db.entities.PipeAppAuthorization;
import org.pdb.db.repo.PipeAppAuthorizationRepo;
import org.pdb.pipes.PipeService;
import org.pdb.pipes.PipeServiceException;
import org.pdb.pipes.RestClient;
import org.springframework.data.cassandra.repository.support.BasicMapId;

/**
 * The services for <a href="https://www.moves-app.com">Moves</a>
 */
public class MovesService implements PipeService {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LogManager.getLogger(MovesService.class);

    private static final String SYNC_URL =
            "https://api.moves-app.com/oauth/v1/authorize?response_type=code&client_id=%s&scope=activity%%20location&redirect_uri=%s";
    private static final String TOKEN_URL =
            "https://api.moves-app.com/oauth/v1/access_token?grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s&redirect_uri=%s";

    /**
     * The Cassandra repo.
     */
    @Inject
    private PipeAppAuthorizationRepo pipeAppAuthorizationRepo;

    /**
     * The REST client for sending requests.
     */
    @Inject
    private RestClient restClient;

    /**
     * Check initialization.
     * @throws AppConfigurationException if initialization issue encountered
     */
    @PostConstruct
    public void checkInit() {
        Helper.checkNullConfig(pipeAppAuthorizationRepo, "pipeAppAuthorizationRepo");
        Helper.checkNullConfig(restClient, "restClient");
    }

    @Override
    public String getId() {
        return "moves";
    }

    @Override
    public URI getSyncUri(String callbackPath) {
        final String methodName = "getSyncUri";

        Helper.logEntrance(LOGGER, methodName, "callbackPath", callbackPath);
        URI response = null;

        PipeAppAuthorization config = pipeAppAuthorizationRepo.findOne(BasicMapId.id().with("id", getId()));
        if (config != null) {
            String path = String.format(SYNC_URL, config.getClientId(), callbackPath);
            response = URI.create(path);
        }

        return Helper.logExit(LOGGER, methodName, response);
    }

    @Override
    public void completeSync(HttpServletRequest request, String originalCallbackPath)
            throws PipeServiceException {
        final String methodName = "completeSync";

        // Get the code.
        String code = request.getParameter("code");
        if (StringUtils.isBlank(code)) {
            throw Helper.logException(LOGGER, methodName, new PipeServiceException(
                    "Missing authorization code."));
        }

        // Exchange code for access token
        PipeAppAuthorization config = pipeAppAuthorizationRepo.findOne(BasicMapId.id().with("id", getId()));
        if (config == null) {
            throw Helper.logException(LOGGER, methodName, new PipeServiceException(
                    "Missing authorization parameters for " + getId()));
        }
        Client client = restClient.getClient();
        String path =
                String.format(TOKEN_URL, code, config.getClientId(), config.getClientSecret(),
                        originalCallbackPath);
        WebTarget webTarget = client.target(path);
        Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(null));

    }

}
