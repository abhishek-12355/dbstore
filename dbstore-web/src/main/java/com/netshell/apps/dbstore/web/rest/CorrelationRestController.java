package com.netshell.apps.dbstore.web.rest;

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
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String retrieveCorrelation(@QueryParam("providerId") String providerId,
                                      @QueryParam("providerEntityId") String providerEntityId,
                                      @QueryParam("consumerId") String consumerId,
                                      @QueryParam("consumerEntityId") String consumerEntityId,
                                      @QueryParam("dispose") boolean dispose) {
        final CorrelationDataBuilder builder = new CorrelationDataBuilder(CorrelationData.CorrelationDataBuilderType.RETRIEVE);
        builder.withConsumerEntityId(consumerEntityId)
                .withConsumerId(consumerId)
                .withProviderEntityId(providerEntityId)
                .withProviderId(providerId)
                .withDispose(dispose);

        return CorrelationUtils.retrieveCorrelation(builder.build());
    }

}
