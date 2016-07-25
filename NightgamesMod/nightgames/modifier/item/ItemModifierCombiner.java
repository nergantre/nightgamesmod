package nightgames.modifier.item;

import nightgames.modifier.ModifierComponentCombiner;

/**
 * TODO: Write class-level documentation.
 */
public class ItemModifierCombiner implements ModifierComponentCombiner<ItemModifier> {
    private static final ItemModifier NULL_MODIFIER = new ItemModifier() {
        private static final String name = "null-item-modifier";

        @Override public String toString() {
            return name;
        }

        @Override public String name() {
            return name;
        }
    };

    private static final ItemModifier TEMPLATE = new ItemModifier() {
        @Override public String toString() {
            return "";
        }

        @Override public String name() {
            return "";
        }

        @Override public ItemModifier combine(ItemModifier next) {
            return next;
        }
    };

    @Override public ItemModifier combine(ItemModifier first, ItemModifier next) {
        return first.combine(next);
    }

    @Override public ItemModifier template() {
        return TEMPLATE;
    }

    @Override public ItemModifier nullModifier() {
        return NULL_MODIFIER;
    }

}
