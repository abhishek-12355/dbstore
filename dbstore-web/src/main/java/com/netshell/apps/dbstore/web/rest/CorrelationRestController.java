package com.netshell.apps.dbstore.web.rest;

import com.netshell.apps.dbstore.api.Correlation;
import com.netshell.apps.dbstore.api.CorrelationData;
import com.netshell.apps.dbstore.api.CorrelationData.CorrelationDataBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCorrelation(@QueryParam("providerId") String providerId,
                                           @QueryParam("providerEntityId") String providerEntityId,
                                           @QueryParam("dispose") boolean dispose,
                                           @QueryParam("dataOnly") boolean dataOnly) {
        final CorrelationDataBuilder builder = new CorrelationDataBuilder(CorrelationData.CorrelationDataBuilderType.RETRIEVE);
        builder.withProviderEntityId(providerEntityId)
                .withProviderId(providerId)
                .withDispose(dispose);

        final Correlation entity = CorrelationUtils.retrieveCorrelation(builder.build());
        return Response.ok(dataOnly ? entity.getData().orElse("") : entity).build();
    }

}
