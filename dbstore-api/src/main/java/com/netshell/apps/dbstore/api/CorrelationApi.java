package com.netshell.apps.dbstore.api;

public interface CorrelationApi {
    void createCorrelation(CorrelationData correlationData);

    Correlation retrieveCorrelation(CorrelationData correlationData);

    void updateCorrelation(CorrelationData correlationData);
}
