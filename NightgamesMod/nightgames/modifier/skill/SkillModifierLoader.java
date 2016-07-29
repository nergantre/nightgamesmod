package nightgames.modifier.skill;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nightgames.modifier.ModifierCategoryLoader;
import nightgames.modifier.ModifierComponentLoader;

/**
 * TODO: Write class-level documentation.
 */
public final class SkillModifierLoader implements ModifierCategoryLoader<SkillModifier> {
    private static final List<ModifierComponentLoader<SkillModifier>> TEMPLATES = Collections.unmodifiableList(
                    Arrays.asList(new BanSkillsModifier(), new BanTacticsModifier(), new EncourageSkillsModifier(),
                                    new EncourageTacticsModifier()));


    @Override public List<ModifierComponentLoader<SkillModifier>> getTemplates() {
        return TEMPLATES;
    }

}
