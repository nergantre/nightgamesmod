package nightgames.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import nightgames.characters.body.mods.ErrorMod;
import nightgames.characters.body.mods.PartMod;

public class PartModAdapter implements JsonSerializer<PartMod>, JsonDeserializer<PartMod> {
    @Override public PartMod deserialize(JsonElement jsonElement, Type type,
                    JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            String modClass = jsonElement.getAsJsonObject().get("_type").getAsString();
            PartMod mod = (PartMod) Class.forName(modClass).newInstance();
            mod.loadData(jsonElement.getAsJsonObject().get("value"));
            return mod;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ErrorMod();
    }

    @Override
    public JsonElement serialize(PartMod mod, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.add("value", mod.saveData());
        object.addProperty("_type", mod.getClass().getCanonicalName());
        return object;
    }
}