package com.netshell.apps.dbstore.api;

public interface CorrelationApi {
    void createCorrelation(CorrelationData correlationData);

    String retrieveCorrelation(CorrelationData correlationData);
}
