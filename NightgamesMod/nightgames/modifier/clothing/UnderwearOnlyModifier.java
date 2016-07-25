package nightgames.modifier.clothing;

import java.util.Collections;
import java.util.Set;

import com.google.gson.JsonObject;

import nightgames.modifier.ModifierComponentLoader;

public class UnderwearOnlyModifier extends ClothingModifier implements ModifierComponentLoader<ClothingModifier> {
    private static final String name = "underwear-only";

    @Override
    public Set<Integer> allowedLayers() {
        return Collections.singleton(0);
    }

    @Override
    public String name() {
        return name;
    }

    public UnderwearOnlyModifier instance(JsonObject object) {
        return new UnderwearOnlyModifier();
    }

    @Override
    public String toString() {
        return name();
    }

}
