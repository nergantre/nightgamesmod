package nightgames.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import nightgames.characters.body.AnalPussyPart;
import nightgames.characters.body.AssPart;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.FacePart;
import nightgames.characters.body.GenericBodyPart;
import nightgames.characters.body.ModdedCockPart;
import nightgames.characters.body.MouthPart;
import nightgames.characters.body.MouthPussyPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.StraponPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.TentaclePart;
import nightgames.characters.body.WingsPart;

public class BodyPartAdapter implements JsonSerializer<BodyPart>, JsonDeserializer<BodyPart> {
    static private Map<String, BodyPart> prototypes;
    static {
        prototypes = new HashMap<>();
        prototypes.put(PussyPart.class.getCanonicalName(), PussyPart.normal);
        prototypes.put(BreastsPart.class.getCanonicalName(), BreastsPart.c);
        prototypes.put(BasicCockPart.class.getCanonicalName(), BasicCockPart.average);
        // for compatibility with < v1.8.1
        prototypes.put(CockPart.class.getCanonicalName(), BasicCockPart.average);
        prototypes.put(ModdedCockPart.class.getCanonicalName(),
                        new ModdedCockPart(BasicCockPart.average, CockMod.bionic));
        prototypes.put(WingsPart.class.getCanonicalName(), WingsPart.demonic);
        prototypes.put(TailPart.class.getCanonicalName(), TailPart.cat);
        prototypes.put(EarPart.class.getCanonicalName(), EarPart.normal);
        prototypes.put(StraponPart.class.getCanonicalName(), StraponPart.generic);
        prototypes.put(TentaclePart.class.getCanonicalName(), new TentaclePart("tentacles", "back", "semen", 0, 1, 1));
        prototypes.put(AssPart.class.getCanonicalName(), new AssPart("ass", 0, 1, 1));
        prototypes.put(MouthPart.class.getCanonicalName(), new MouthPart("mouth", 0, 1, 1));
        prototypes.put(AnalPussyPart.class.getCanonicalName(), new AnalPussyPart());
        prototypes.put(MouthPussyPart.class.getCanonicalName(), new MouthPussyPart());
        prototypes.put(GenericBodyPart.class.getCanonicalName(), new GenericBodyPart("", 0, 1, 1, "none", "none"));
        prototypes.put(FacePart.class.getCanonicalName(), new FacePart(.1, 2.3));
    }

    @Override
    public BodyPart deserialize(JsonElement jsonElement, Type type,
                    JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        String classType = obj.get("class").getAsString();
        return prototypes.get(classType)
                         .load(obj);
    }

    @Override
    public JsonElement serialize(BodyPart part, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = part.save();
        obj.addProperty("class", part.getClass()
                             .getCanonicalName());
        return obj;
    }
}
