package nightgames.modifier.clothing;

import java.util.Collections;
import java.util.Set;

import org.json.simple.JSONObject;

import nightgames.modifier.ModifierComponent;

public class NudeModifier extends ClothingModifier implements ModifierComponent<NudeModifier> {

    @Override
    public Set<Integer> allowedLayers() {
        return Collections.emptySet();
    }

    @Override
    public String name() {
        return "nude";
    }

    @Override
    public NudeModifier instance(JSONObject obj) {
        return new NudeModifier();
    }

    @Override
    public String toString() {
        return name();
    }

}
