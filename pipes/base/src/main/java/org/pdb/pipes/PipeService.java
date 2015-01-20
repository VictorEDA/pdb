package org.pdb.pipes;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for a data pipe.
 */
public interface PipeService {

    String getId();

    URI getSyncUri(HttpServletRequest request);

}
