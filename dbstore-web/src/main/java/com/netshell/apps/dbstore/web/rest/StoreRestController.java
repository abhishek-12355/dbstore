package com.netshell.apps.dbstore.web.rest;

import com.netshell.libraries.dbmodules.dbcommon.util.DBUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

@Path("/store")
public class StoreRestController {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveString(String object, @Context UriInfo uriInfo) {
        final URI id = uriInfo.getAbsolutePathBuilder()
                .path(StoreRestController.class, "retrieveString")
                .build(DBStoreUtils.saveString(object));
        return Response.created(id).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response saveObject(InputStream inputStream, @Context UriInfo uriInfo) {
        final URI id = uriInfo.getAbsolutePathBuilder()
                .path(StoreRestController.class, "retrieveString")
                .build(DBStoreUtils.saveStream(inputStream));
        return Response.created(id).build();
    }

    @GET
    @Path("/{storeId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String retrieveString(@PathParam("storeId") String id) {
        return DBStoreUtils.retrieveString(id);
    }

    @GET
    @Path("/{storeId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public StreamingOutput retrieveObject(@PathParam("storeId") String id) {
        return outputStream -> {
            try {
                DBStoreUtils.retrieveStream(id, outputStream);
                DBUtil.closeConnection(true);
            } catch (Throwable e){
                DBUtil.closeConnection(false);
                throw e;
            }
        };
    }
}
