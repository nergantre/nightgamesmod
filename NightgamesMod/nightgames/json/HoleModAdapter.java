package nightgames.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import nightgames.characters.body.mods.ErrorHoleMod;
import nightgames.characters.body.mods.HoleMod;

public class HoleModAdapter implements JsonSerializer<HoleMod>, JsonDeserializer<HoleMod> {
    @Override public HoleMod deserialize(JsonElement jsonElement, Type type,
                    JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            @SuppressWarnings("unchecked")
            Class<HoleMod> modClass = (Class<HoleMod>) Class.forName(jsonElement.getAsString());
            return modClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ErrorHoleMod();
    }

    @Override
    public JsonElement serialize(HoleMod mod, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(mod.getClass().getCanonicalName());
    }
}
