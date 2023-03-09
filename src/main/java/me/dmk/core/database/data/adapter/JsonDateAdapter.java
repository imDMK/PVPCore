package me.dmk.core.database.data.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by DMK on 09.03.2023
 */

public class JsonDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String jsonElementAsString = jsonElement.getAsString();

        return new Date(
                Long.parseLong(jsonElementAsString)
        );
    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(
                String.valueOf(date.getTime())
        );
    }
}
