package com.netshell.apps.dbstore.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.netshell.libraries.utilities.services.encoder.EncoderService;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DBStoreMetadata implements Serializable {
    private final String id;
    private final Type type;

    @JsonIgnore
    private final transient byte[] payload;
    private final LocalDateTime dateTime;

    public DBStoreMetadata(String id, Type type, byte[] payload, LocalDateTime dateTime) {
        this.id = id;
        this.type = type;
        this.payload = payload;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }

    @JsonInclude
    public String getEncodedPayload() {
        return new String(EncoderService.encode(payload));
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "DBStoreMetadata{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", payload=" + getEncodedPayload() +
                ", dateTime=" + dateTime +
                '}';
    }
}
