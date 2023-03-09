package me.dmk.core.database.data.serializer;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;

/**
 * Created by DMK on 09.03.2023
 */

@AllArgsConstructor
public class GsonSerializerImpl implements GsonSerializer {

    private final Gson gson;

    @Override
    public <V> String serialize(V entity) {
        return this.gson.toJson(entity);
    }

    @Override
    public <V> V deserialize(String json, Class<V> vClass) {
        return this.gson.fromJson(json, vClass);
    }
}
