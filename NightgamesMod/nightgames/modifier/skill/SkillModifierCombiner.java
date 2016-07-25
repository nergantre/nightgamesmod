package nightgames.modifier.skill;

import nightgames.modifier.ModifierComponentCombiner;

/**
 * TODO: Write class-level documentation.
 */
public final class SkillModifierCombiner implements ModifierComponentCombiner<SkillModifier> {
    private static final SkillModifier NULL_MODIFIER = new SkillModifier() {
        private static final String name = "null-skill-modifier";

        @Override public String name() {
            return name;
        }

        @Override public String toString() {
            return name;
        }
    };

    private static final SkillModifier TEMPLATE = new SkillModifier() {
        @Override public String toString() {
            return "";
        }

        @Override public String name() {
            return "";
        }

        @Override public SkillModifier combine(SkillModifier next) {
            return next;
        }
    };

    @Override public SkillModifier combine(SkillModifier first, SkillModifier next) {
        return first.combine(next);
    }

    @Override public SkillModifier template() {
        return TEMPLATE;
    }

    @Override public SkillModifier nullModifier() {
        return NULL_MODIFIER;
    }
}
