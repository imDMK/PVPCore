package me.dmk.core.database.data.serializer;

/**
 * Created by DMK on 09.03.2023
 */

public interface GsonSerializer {

    <V> String serialize(V entity);

    <V> V deserialize(String json, Class<V> vClass);
}
