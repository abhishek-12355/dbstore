package com.netshell.apps.dbstore.web.rest.filters;

import com.netshell.libraries.dbmodules.dbcommon.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class DBCloseFilter implements ContainerResponseFilter {
    private static final Logger logger = LoggerFactory.getLogger(DBCloseFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        final Response.Status status = Response.Status.fromStatusCode(containerResponseContext.getStatus());
        final boolean commit = Response.Status.Family.SUCCESSFUL == status.getFamily();
        logger.info("Closing DB Connection: " + commit);
        DBUtil.closeConnection(commit);
    }
}
