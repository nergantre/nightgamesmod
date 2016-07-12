package nightgames.modifier.clothing;

import com.google.gson.JsonObject;
import nightgames.modifier.ModifierCategoryLoader;
import nightgames.modifier.ModifierComponentLoader;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Write class-level documentation.
 */
public class ClothingModifierLoader implements ModifierCategoryLoader<ClothingModifier> {
    private static final List<ModifierComponentLoader<ClothingModifier>> TEMPLATES = Collections.unmodifiableList(
                    Arrays.asList(new ForceClothingModifier(), new NoPantiesModifier(), new NudeModifier(),
                                    new UnderwearOnlyModifier()));

    @Override public Collection<ModifierComponentLoader<ClothingModifier>> getTemplates() {
        return TEMPLATES;
    }
}
