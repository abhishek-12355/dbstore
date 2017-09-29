package com.netshell.apps.dbstore.lib;

import com.netshell.apps.dbstore.api.CorrelationApi;
import com.netshell.apps.dbstore.api.CorrelationData;
import com.netshell.libraries.dbmodules.dbcommon.util.DBUtil;
import com.netshell.libraries.utilities.common.CommonMethods;
import com.netshell.libraries.utilities.services.id.IDGeneratorService;

import java.util.Arrays;
import java.util.List;

public class CorrelationImpl implements CorrelationApi {

    private static final String NS_CORRELATION = "ns_correlation";
    private static final String QUERY_INSERT = "INSERT INTO ns_correlation values(?,?,?,?,?)";
    private static final String QUERY_RETRIEVE = "SELECT correlation_data FROM ns_correlation where provider_id=? and provider_entity_id=? and consumer_id=? and consumer_entity_id=?";
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
                CommonMethods.checkInput(correlationData.getConsumerEntityId(), (String) null),
                correlationData.getData()
        );
    }

    @Override
    public String retrieveCorrelation(CorrelationData correlationData) {
        final DBUtil.Holder<String> holder = new DBUtil.Holder<>();
        final List<String> retrieveList = createRetrieveList(correlationData);
        DBUtil.executeQuery(QUERY_RETRIEVE, retrieveList, resultSet -> {
            if (resultSet.next()) {
                holder.item = resultSet.getString("correlation_data");
            }
        });

        if (holder.item == null) {
            throw new RuntimeException("Not Found");
        }

        if (correlationData.isDispose()) {
            DBUtil.executeUpdate(QUERY_DELETE, retrieveList);
        }

        return holder.item;
    }

    private List<String> createRetrieveList(CorrelationData correlationData) {
        return Arrays.asList(
                correlationData.getProviderId(),
                correlationData.getProviderEntityId(),
                correlationData.getConsumerId(),
                CommonMethods.checkInputDefault(correlationData.getConsumerEntityId(), null)
        );
    }
}
