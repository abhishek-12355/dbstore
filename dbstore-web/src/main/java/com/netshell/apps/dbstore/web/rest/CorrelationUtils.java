package com.netshell.apps.dbstore.web.rest;

import com.netshell.apps.dbstore.api.Correlation;
import com.netshell.apps.dbstore.api.CorrelationApi;
import com.netshell.apps.dbstore.api.CorrelationData;
import com.netshell.apps.dbstore.lib.CorrelationImpl;

public class CorrelationUtils {

    private static final CorrelationApi API = new CorrelationImpl();

    public static void createCorrelation(CorrelationData correlationData) {
        API.createCorrelation(correlationData);
    }

    public static Correlation retrieveCorrelation(CorrelationData correlationData) {
        return API.retrieveCorrelation(correlationData);
    }

}
