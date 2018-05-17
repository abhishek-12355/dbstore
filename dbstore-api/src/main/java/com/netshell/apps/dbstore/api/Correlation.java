package com.netshell.apps.dbstore.api;

import java.io.Serializable;
import java.util.Optional;

public final class Correlation implements Serializable {
  private final String data;
  private final String consumerId;
  private final String consumerEntityId;

  public Correlation(String data, String consumerId, String consumerEntityId) {
    this.data = data;
    this.consumerId = consumerId;
    this.consumerEntityId = consumerEntityId;
  }

  public Optional<String> getData() {
    return Optional.of(data);
  }

  public Optional<String> getConsumerId() {
    return Optional.of(consumerId);
  }

  public Optional<String> getConsumerEntityId() {
    return Optional.of(consumerEntityId);
  }

  @Override
  public String toString() {
    return "Correlation{" +
            "data='" + data + '\'' +
            ", consumerId='" + consumerId + '\'' +
            ", consumerEntityId='" + consumerEntityId + '\'' +
            '}';
  }
}
