package org.pdb.config;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.pdb.common.AppException;
import org.pdb.common.rest.ErrorJson;


/**
 * Convert exceptions into a JSON response.
 */
@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

    /**
     * Default constructor.
     */
    public AppExceptionMapper() {
        // empty
    }

    @Override
    public Response toResponse(AppException exception) {
        ErrorJson json = new ErrorJson("error", exception.getMessage());
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(json).build();
    }
}
