package com.netshell.apps.dbstore.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netshell.apps.dbstore.api.Correlation;
import com.netshell.apps.dbstore.api.CorrelationData;
import com.netshell.apps.dbstore.api.CorrelationData.CorrelationDataBuilder;
import com.netshell.libraries.utilities.common.JsonUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/correlation")
public class CorrelationRestController {

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createCorrelation(String data,
                                      @QueryParam("providerId") String providerId,
                                      @QueryParam("providerEntityId") String providerEntityId,
                                      @QueryParam("consumerId") String consumerId,
                                      @QueryParam("consumerEntityId") String consumerEntityId) {

        final CorrelationDataBuilder builder = new CorrelationDataBuilder(CorrelationData.CorrelationDataBuilderType.SAVE);
        builder.withConsumerEntityId(consumerEntityId)
                .withConsumerId(consumerId)
                .withProviderEntityId(providerEntityId)
                .withProviderId(providerId)
                .withData(data);

        CorrelationUtils.createCorrelation(builder.build());
        return Response.noContent().build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response retrieveCorrelation(@QueryParam("providerId") String providerId,
                                        @QueryParam("providerEntityId") String providerEntityId,
                                        @QueryParam("dispose") boolean dispose,
                                        @QueryParam("dataOnly") boolean dataOnly) {
        final CorrelationDataBuilder builder = new CorrelationDataBuilder(CorrelationData.CorrelationDataBuilderType.RETRIEVE);
        builder.withProviderEntityId(providerEntityId)
                .withProviderId(providerId)
                .withDispose(dispose);

        try {
            final Correlation entity = CorrelationUtils.retrieveCorrelation(builder.build());
            if (dataOnly) {
                return Response.ok(entity.getData().orElse("")).type(MediaType.TEXT_PLAIN).build();
            } else {
                return Response.ok(JsonUtils.writeValueAsString(entity)).type(MediaType.APPLICATION_JSON).build();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
