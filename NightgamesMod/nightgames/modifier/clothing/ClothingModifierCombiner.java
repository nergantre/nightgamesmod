package nightgames.modifier.clothing;

import nightgames.modifier.ModifierComponentCombiner;

/**
 * TODO: Write class-level documentation.
 */
public final class ClothingModifierCombiner implements ModifierComponentCombiner<ClothingModifier> {
    private static final ClothingModifier TEMPLATE = new ClothingModifier() {
        @Override public String toString() {
            return "";
        }

        @Override public String name() {
            return "";
        }

        @Override public ClothingModifier combine(ClothingModifier next) {
            return next;
        }
    };

    private static final ClothingModifier NULL_MODIFIER = new ClothingModifier() {
        private static final String name = "null-clothing-modifier";

        @Override
        public String toString() {
            return name;
        }

        @Override public String name() {
            return name;
        }
    };

    @Override public ClothingModifier combine(ClothingModifier first, ClothingModifier next) {
        return first.combine(next);
    }

    @Override public ClothingModifier template() {
        return TEMPLATE;
    }

    @Override public ClothingModifier nullModifier() {
        return NULL_MODIFIER;
    }
}
