package nightgames.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import nightgames.items.clothing.Clothing;

/**
 * Gson TypeAdapter for Clothing that serializes and deserializes based on the Clothing's ID.
 */
public class ClothingAdaptor implements JsonSerializer<Clothing>, JsonDeserializer<Clothing> {

    @Override public Clothing deserialize(JsonElement jsonElement, Type type,
                    JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Clothing.getByID(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Clothing clothing, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(clothing.getID());
    }
}
