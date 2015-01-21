package org.pdb.pipes;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for a data pipe.
 */
public interface PipeService {

    String getId();

    URI getSyncUri(String callbackPath);

    void completeSync(HttpServletRequest request, String originalCallbackPath) throws PipeServiceException;

}
