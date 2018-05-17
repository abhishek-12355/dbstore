package com.netshell.apps.dbstore.web.rest;

import com.netshell.apps.dbstore.api.DBStoreApi;
import com.netshell.apps.dbstore.api.DBStoreMetadata;
import com.netshell.apps.dbstore.lib.DbStoreImpl;
import com.netshell.libraries.utilities.common.IOUtils;
import com.netshell.libraries.utilities.common.Wrapper;

import java.io.InputStream;
import java.io.OutputStream;

import static com.netshell.libraries.utilities.common.Wrapper.wrapConsumer;

public class DBStoreUtils {

    private static final DBStoreApi API = new DbStoreImpl();

    public static String saveString(String object) {
        return API.saveString(object);
    }

    public static String retrieveString(String id) {
        return API.retrieveString(id);
    }

    public static String saveStream(InputStream stream) {
        return API.saveStream(stream);
    }

    public static void retrieveStream(String id, OutputStream outputStream) {
        API.retrieveStream(id, inputStream -> wrapConsumer(() -> IOUtils.inputToOutputStream(inputStream, outputStream)));
    }

    public static void delete(String id) {
        API.delete(id);
    }

    public static DBStoreMetadata retrieveWithMetadata(String id) {
        return API.retrieveWithMetadata(id);
    }
}
