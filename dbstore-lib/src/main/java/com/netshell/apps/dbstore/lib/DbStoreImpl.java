package com.netshell.apps.dbstore.lib;

import com.netshell.apps.dbstore.api.DBStoreApi;
import com.netshell.apps.dbstore.api.DBStoreMetadata;
import com.netshell.libraries.dbmodules.dbcommon.util.DBUtil;
import com.netshell.apps.dbstore.api.Type;
import com.netshell.libraries.utilities.common.CommonUtils;
import com.netshell.libraries.utilities.common.IOUtils;
import com.netshell.libraries.utilities.services.id.IDGeneratorService;

import java.io.*;
import java.sql.Blob;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.function.Consumer;

public class DbStoreImpl implements DBStoreApi {

    private static final String NS_DBSTORE = "ns_dbstore";
    private static final String QUERY_INSERT = "INSERT INTO ns_dbstore values(?,?,?,?)";
    private static final String QUERY_RETRIEVE = "SELECT * FROM ns_dbstore where store_id=?";
    private static final String QUERY_DELETE = "DELETE FROM ns_dbstore where store_id=?";

    @Override
    public String saveString(String object) {
        return saveObject(object, Type.STRING);
    }

    @Override
    public String saveStream(InputStream object) {
        return saveObject(object, Type.STREAM);
    }

    @Override
    public String retrieveString(String id) {
        DBUtil.Holder<String> holder = new DBUtil.Holder<>();
        retrieveStream(id, inputStream -> {
            try {
                final Object o = IOUtils.toStreamObject(inputStream);
                holder.item = String.class.cast(o);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
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

    @Override
    public void delete(String id) {
        final int i = DBUtil.executeUpdate(
                QUERY_DELETE,
                Collections.singletonList(id)
        );

        if (i != 1) {
            throw new RuntimeException("Unable to delete");
        }
    }

    @Override
    public DBStoreMetadata retrieveWithMetadata(String id) {
        DBUtil.Holder<DBStoreMetadata> holder = new DBUtil.Holder<>();
        DBUtil.executeQuery(
                QUERY_RETRIEVE,
                Collections.singletonList(id),
                resultSet -> {
                    if(resultSet.next()) {
                        final Type type = Type.valueOf(resultSet.getString("type"));
                        final LocalDateTime dateTime = resultSet.getTimestamp("created").toLocalDateTime();
                        final Blob payload = resultSet.getBlob("payload");
                        byte[] bytes;
                        try {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            IOUtils.inputToOutputStream(payload.getBinaryStream(), outputStream);
                            bytes = outputStream.toByteArray();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            payload.free();
                        }

                        holder.item = new DBStoreMetadata(id, type, bytes, dateTime);
                    }
                }
        );
        return holder.item;
    }

    private static String saveObject(Object object, Type type) {
        final String id = IDGeneratorService.create(NS_DBSTORE);
        try {
            final int i = DBUtil.executeUpdate(
                    QUERY_INSERT,
                    Arrays.asList(id, type.name(), createStream(object), Calendar.getInstance().getTime())
            );

            if (i != 1) {
                throw new RuntimeException("Unable to insert");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    private static InputStream createStream(Object object) throws IOException {
        final InputStream stream;
        if ((object instanceof InputStream)) {
            stream = (InputStream) object;
        } else {
            final ByteArrayOutputStream outputStream = IOUtils.toObjectStream(object);
            stream = new ByteArrayInputStream(outputStream.toByteArray());
        }

        return stream;
    }
}
