package nightgames.modifier.action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import nightgames.modifier.ModifierCategoryLoader;
import nightgames.modifier.ModifierComponentLoader;

/**
 * TODO: Write class-level documentation.
 */
public class ActionModifierLoader implements ModifierCategoryLoader<ActionModifier> {
    public static final List<ModifierComponentLoader<ActionModifier>> TEMPLATES =
                    Collections.singletonList(new BanActionModifier());

    @Override public Collection<ModifierComponentLoader<ActionModifier>> getTemplates() {
        return TEMPLATES;
    }
}
