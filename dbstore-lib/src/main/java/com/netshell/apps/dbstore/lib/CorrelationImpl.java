package com.netshell.apps.dbstore.lib;

import com.netshell.apps.dbstore.api.Correlation;
import com.netshell.apps.dbstore.api.CorrelationApi;
import com.netshell.apps.dbstore.api.CorrelationData;
import com.netshell.libraries.dbmodules.dbcommon.util.DBUtil;

import java.util.Arrays;
import java.util.List;

public class CorrelationImpl implements CorrelationApi {

    private static final String NS_CORRELATION = "ns_correlation";
    private static final String QUERY_INSERT = "INSERT INTO ns_correlation values(?,?,?,?,?)";
    private static final String QUERY_UPDATE = "UPDATE ns_correlation set consumer_id=?, consumer_entity_id=?, correlation_data=? where provider_id=? and provider_entity_id=?";
    private static final String QUERY_RETRIEVE = "SELECT correlation_data, consumer_id, consumer_entity_id FROM ns_correlation where provider_id=? and provider_entity_id=?";
    private static final String QUERY_DELETE = "DELETE FROM ns_correlation where provider_id=? and provider_entity_id=? and consumer_id=? and consumer_entity_id=?";


    @Override
    public void createCorrelation(CorrelationData correlationData) {
        final int i = DBUtil.executeUpdate(QUERY_INSERT, createInsertList(correlationData));
        if (i != 1) {
            throw new RuntimeException("Unable to insert");
        }
    }

    private List<String> createInsertList(CorrelationData correlationData) {
        return Arrays.asList(
                correlationData.getProviderId(),
                correlationData.getProviderEntityId(),
                correlationData.getConsumerId(),
                correlationData.getConsumerEntityId(),
                correlationData.getData()
        );
    }

    @Override
    public Correlation retrieveCorrelation(CorrelationData correlationData) {
        final DBUtil.Holder<Correlation> holder = new DBUtil.Holder<>();
        final List<String> retrieveList = createRetrieveList(correlationData);
        DBUtil.executeQuery(QUERY_RETRIEVE, retrieveList, resultSet -> {
            if (resultSet.next()) {
                holder.item = new Correlation(
                        resultSet.getString("correlation_data"),
                        resultSet.getString("consumer_id"),
                        resultSet.getString("consumer_entity_id")
                );

            }
        });

        if (holder.item == null) {
            return null;
        }

        if (correlationData.isDispose()) {
            DBUtil.executeUpdate(QUERY_DELETE, retrieveList);
        }

        return holder.item;
    }

    @Override
    public void updateCorrelation(CorrelationData correlationData) {
        final int i = DBUtil.executeUpdate(QUERY_UPDATE, createUpdateList(correlationData));
        if (i != 1) {
            throw new RuntimeException("Unable to insert");
        }
    }

    private List<String> createUpdateList(CorrelationData correlationData) {
        return Arrays.asList(
                correlationData.getConsumerId(),
                correlationData.getConsumerEntityId(),
                correlationData.getData(),
                correlationData.getProviderId(),
                correlationData.getProviderEntityId()
        );
    }

    private List<String> createRetrieveList(CorrelationData correlationData) {
        return Arrays.asList(
                correlationData.getProviderId(),
                correlationData.getProviderEntityId()
        );
    }
}
