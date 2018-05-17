package com.netshell.apps.dbstore.web.rest;

import com.netshell.libraries.dbmodules.dbcommon.util.DBUtil;
import com.netshell.libraries.utilities.common.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

@Path("/store")
public class StoreRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreRestController.class);

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveString(String object, @Context UriInfo uriInfo) {
        try {
            final URI id = uriInfo.getAbsolutePathBuilder()
                    .path(StoreRestController.class, "retrieveString")
                    .build(DBStoreUtils.saveString(object));
            return Response.created(id).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response saveObject(InputStream inputStream, @Context UriInfo uriInfo) {
        try {
            final URI id = uriInfo.getAbsolutePathBuilder()
                    .path(StoreRestController.class, "retrieveString")
                    .build(DBStoreUtils.saveStream(inputStream));
            return Response.created(id).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{storeId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response retrieveString(@PathParam("storeId") String id) {
        try {
            return Response.ok(DBStoreUtils.retrieveString(id)).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{storeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveWithMetadata(@PathParam("storeId") String id) {
        try {
            return Response.ok(JsonUtils.writeValueAsString(DBStoreUtils.retrieveWithMetadata(id))).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{storeId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response retrieveObject(@PathParam("storeId") String id) {
        try {
            final StreamingOutput output = outputStream -> {
                try {
                    DBStoreUtils.retrieveStream(id, outputStream);
                    DBUtil.closeConnection(true);
                } catch (Throwable e) {
                    // Connection will have to explicitly closed because stream is not closed until response is sent
                    DBUtil.closeConnection(false);
                    throw e;
                }
            };
            return Response.ok(output).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{storeId}")
    public Response delete(@PathParam("storeId") String id) {
        try {
            DBStoreUtils.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
