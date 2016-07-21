package nightgames.modifier.clothing;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nightgames.json.JsonUtils;
import nightgames.modifier.ModifierComponentLoader;

public class ForceClothingModifier extends ClothingModifier implements ModifierComponentLoader<ClothingModifier> {
    private final String name = "force-clothing";

    private final Set<String> ids;

    public ForceClothingModifier(String... ids) {
        this.ids = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(ids)));
    }

    @Override public Set<String> forcedItems() {
        return ids;
    }

    @Override public String name() {
        return name;
    }

    @Override public String toString() {
        return "Forced:" + ids.toString();
    }

    public ForceClothingModifier instance(JsonObject object) {
        JsonElement element = JsonUtils.getOptional(object, "clothing").orElseThrow(() -> new IllegalArgumentException(
                        "'force-clothing' element must have 'clothing' item"));
        if (element.isJsonPrimitive()) {
            return new ForceClothingModifier(element.getAsString());
        }
        return new ForceClothingModifier(JsonUtils.stringsFromJson(element.getAsJsonArray()).toArray(new String[] {}));
    }
}
