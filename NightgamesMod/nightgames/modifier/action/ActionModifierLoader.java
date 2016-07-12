package nightgames.modifier.action;

import nightgames.modifier.ModifierCategoryLoader;
import nightgames.modifier.ModifierComponentLoader;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Write class-level documentation.
 */
public class ActionModifierLoader implements ModifierCategoryLoader<ActionModifier> {
    public static final List<ModifierComponentLoader<ActionModifier>> TEMPLATES = Collections.singletonList(new BanActionModifier());

    @Override public Collection<ModifierComponentLoader<ActionModifier>> getTemplates() {
        return TEMPLATES;
    }
}
