package nightgames.modifier.clothing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.simple.JSONObject;

import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.modifier.ModifierComponent;

public class NoPantiesModifier extends ClothingModifier implements ModifierComponent<NoPantiesModifier> {

    private static final Set<Integer> ALL_BUT_ZERO = Collections
                    .unmodifiableSet(IntStream.range(1, Clothing.N_LAYERS).boxed().collect(Collectors.toSet()));

    @Override
    public Map<ClothingSlot, Set<Integer>> allowedSlotLayerCombos() {
        Map<ClothingSlot, Set<Integer>> combos = new HashMap<>(ALL_SLOT_LAYER_COMBOS);
        combos.replace(ClothingSlot.bottom, ALL_BUT_ZERO);
        return combos;
    }

    @Override
    public String name() {
        return "no-panties";
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public NoPantiesModifier instance(JSONObject obj) {
        return new NoPantiesModifier();
    }

}
