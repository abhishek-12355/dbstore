package com.netshell.apps.dbstore.api;

import java.io.InputStream;
import java.io.Serializable;
import java.util.function.Consumer;

public interface DBStoreApi {
    String saveString(String object);
    String saveStream(InputStream object);

    String retrieveString(String id);
    void retrieveStream(String id, Consumer<InputStream> consumer);

    void delete(String id);

    DBStoreMetadata retrieveWithMetadata(String id);
}
