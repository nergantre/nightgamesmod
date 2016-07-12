package nightgames.modifier.clothing;

import java.util.Collections;
import java.util.Set;

import com.google.gson.JsonObject;
import nightgames.modifier.ModifierComponentLoader;

public class NudeModifier extends ClothingModifier implements ModifierComponentLoader<ClothingModifier> {
    private static final String name = "nude";

    @Override
    public Set<Integer> allowedLayers() {
        return Collections.emptySet();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public NudeModifier instance(JsonObject object) {
        return new NudeModifier();
    }

    @Override
    public String toString() {
        return name();
    }

}
