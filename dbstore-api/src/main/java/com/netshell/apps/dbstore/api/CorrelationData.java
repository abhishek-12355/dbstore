package com.netshell.apps.dbstore.api;

import com.netshell.libraries.utilities.common.CommonMethods;

public class CorrelationData {
    private String data;
    private String providerId;
    private String providerEntityId;
    private String consumerId;
    private String consumerEntityId;
    private boolean dispose;

    public String getData() {
        return data;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getProviderEntityId() {
        return providerEntityId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getConsumerEntityId() {
        return consumerEntityId;
    }

    public boolean isDispose() {
        return dispose;
    }

    public static enum CorrelationDataBuilderType {
        SAVE, RETRIEVE
    }

    public static class CorrelationDataBuilder {
        private final CorrelationDataBuilderType type;

        private CorrelationData data = new CorrelationData();

        public CorrelationDataBuilder(CorrelationDataBuilderType type) {
            this.type = type;
        }

        public CorrelationDataBuilder withData(String data) {
            if (type == CorrelationDataBuilderType.SAVE){
                CommonMethods.checkInput(data);
            }

            this.data.data = data;
            return this;
        }

        public CorrelationDataBuilder withProviderId(String providerId) {
            CommonMethods.checkInput(providerId);
            this.data.providerId = providerId;
            return this;
        }

        public CorrelationDataBuilder withProviderEntityId(String providerEntityId) {
            CommonMethods.checkInput(providerEntityId);
            this.data.providerEntityId = providerEntityId;
            return this;
        }

        public CorrelationDataBuilder withConsumerId(String consumerId) {
            CommonMethods.checkInput(consumerId);
            this.data.consumerId = consumerId;
            return this;
        }

        public CorrelationDataBuilder withConsumerEntityId(String consumerEntityId) {
            this.data.consumerEntityId = consumerEntityId;
            return this;
        }

        public CorrelationDataBuilder withDispose(boolean dispose) {
            this.data.dispose = dispose;
            return this;
        }

        public CorrelationData build() {
            return data;
        }
    }
}
