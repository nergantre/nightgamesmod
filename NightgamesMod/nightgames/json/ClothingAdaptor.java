package nightgames.json;

import com.google.gson.*;
import nightgames.items.clothing.Clothing;

import java.lang.reflect.Type;

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
