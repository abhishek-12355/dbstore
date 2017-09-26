package com.netshell.apps.dbstore.web.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;

@Path("/correlation")
public class CorrelationRestController {

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createCorrelation(String data,
                                      @QueryParam("providerId") String providerId,
                                      @QueryParam("bucket") String bucket,
                                      @QueryParam("consumerId") String consumerId) {
        return Response.created(null).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String retrieveCorrelation(@QueryParam("providerId") String providerId,
                                      @QueryParam("bucket") String bucket,
                                      @QueryParam("consumerId") String consumerId,
                                      @QueryParam("dispose") boolean dispose) {
        return null;
    }

}
