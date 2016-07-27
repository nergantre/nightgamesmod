package nightgames.modifier.clothing;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import nightgames.modifier.ModifierCategoryLoader;
import nightgames.modifier.ModifierComponentLoader;

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
