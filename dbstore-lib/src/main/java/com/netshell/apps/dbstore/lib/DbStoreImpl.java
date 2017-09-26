package com.netshell.apps.dbstore.lib;

import com.netshell.apps.dbstore.api.DBStoreApi;
import com.netshell.libraries.dbmodules.dbcommon.util.DBUtil;
import com.netshell.apps.dbstore.api.Type;
import com.netshell.libraries.utilities.common.IOUtils;
import com.netshell.libraries.utilities.services.id.IDGeneratorService;

import java.io.*;
import java.sql.Blob;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.function.Consumer;

public class DbStoreImpl implements DBStoreApi {

    private static final String NS_DBSTORE = "NS_DBSTORE";
    private static final String QUERY_INSERT = "INSERT INTO NS_DBSTORE values(?,?,?,?)";
    private static final String QUERY_RETRIEVE = "SELECT * FROM NS_DBSTORE where store_id=?";

    @Override
    public String saveString(String object) {
        final String id = IDGeneratorService.create(NS_DBSTORE);
        final int i = DBUtil.executeUpdate(
                QUERY_INSERT,
                Arrays.asList(id, Type.OBJECT.name(), object, Calendar.getInstance().getTime())
        );

        if (i != 1) {
            throw new RuntimeException("Unable to insert");
        }

        return id;
    }

    @Override
    public String saveStream(InputStream object) {
        final String id = IDGeneratorService.create(NS_DBSTORE);
        final int i = DBUtil.executeUpdate(
                QUERY_INSERT,
                Arrays.asList(id, Type.OBJECT.name(), object, Calendar.getInstance().getTime())
        );

        if (i != 1) {
            throw new RuntimeException("Unable to insert");
        }

        return id;
    }

    @Override
    public String retrieveString(String id) {
        DBUtil.Holder<String> holder = new DBUtil.Holder<>();
        retrieveStream(id, inputStream -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                IOUtils.inputToOutputStream(inputStream, outputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            holder.item = new String(outputStream.toByteArray());
        });
        return holder.item;
    }

    @Override
    public void retrieveStream(String id, Consumer<InputStream> consumer) {
        DBUtil.executeQuery(
                QUERY_RETRIEVE,
                Collections.singletonList(id),
                resultSet -> {
                    if(resultSet.next()) {
                        final Blob payload = resultSet.getBlob("payload");
                        try {
                            consumer.accept(payload.getBinaryStream());
                        } finally {
                            payload.free();
                        }
                    }
                }
        );
    }
}
