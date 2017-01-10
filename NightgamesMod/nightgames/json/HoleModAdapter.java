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
import nightgames.characters.body.mods.PartMod;

public class HoleModAdapter implements JsonSerializer<PartMod>, JsonDeserializer<PartMod> {
    @Override public PartMod deserialize(JsonElement jsonElement, Type type,
                    JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            @SuppressWarnings("unchecked")
            Class<PartMod> modClass = (Class<PartMod>) Class.forName(jsonElement.getAsString());
            return modClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ErrorHoleMod();
    }

    @Override
    public JsonElement serialize(PartMod mod, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(mod.getClass().getCanonicalName());
    }
}
