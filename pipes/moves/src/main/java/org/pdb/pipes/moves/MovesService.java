package org.pdb.pipes.moves;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pdb.common.AppConfigurationException;
import org.pdb.common.Helper;
import org.pdb.db.entities.PipeAppAuthorization;
import org.pdb.db.repo.PipeAppAuthorizationRepo;
import org.pdb.pipes.PipeService;
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

    /**
     * The Cassandra repo.
     */
    @Inject
    private PipeAppAuthorizationRepo pipeAppAuthorizationRepo;

    /**
     * Check initialization.
     * @throws AppConfigurationException if initialization issue encountered
     */
    @PostConstruct
    public void checkInit() {
        Helper.checkNullConfig(pipeAppAuthorizationRepo, "pipeAppAuthorizationRepo");
    }

    @Override
    public String getId() {
        return "moves";
    }

    @Override
    public URI getSyncUri(HttpServletRequest request) {
        final String methodName = "getSyncUri";

        Helper.logEntrance(LOGGER, methodName, "request", request);
        URI response = null;

        PipeAppAuthorization config = pipeAppAuthorizationRepo.findOne(BasicMapId.id().with("id", getId()));
        if (config != null) {
            StringBuilder sb = new StringBuilder();
            if (request.isSecure()) {
                sb.append("https://");
            } else {
                sb.append("http://");
            }
            sb.append(request.getServerName());
            int port = request.getServerPort();
            if (port != 80 && port != 443 && port != 0) {
                sb.append(':');
                sb.append(port);
            }
            sb.append(request.getContextPath());
            sb.append("/api/pipes/");
            sb.append(getId());
            sb.append("/callback");

            String path = String.format(SYNC_URL, config.getClientId(), sb.toString());
            response = URI.create(path);
        }

        return Helper.logExit(LOGGER, methodName, response);
    }

}
