package nightgames.modifier.item;

import nightgames.modifier.ModifierCategoryLoader;
import nightgames.modifier.ModifierComponentLoader;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Write class-level documentation.
 */
public class ItemModifierLoader implements ModifierCategoryLoader<ItemModifier> {
    private static final List<ModifierComponentLoader<ItemModifier>> TEMPLATES =
                    Collections.unmodifiableList(Arrays.asList(new BanToysModifier(), new BanConsumablesModifier()));

    @Override public Collection<ModifierComponentLoader<ItemModifier>> getTemplates() {
        return TEMPLATES;
    }
}
