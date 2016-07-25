package nightgames.modifier.action;

import nightgames.modifier.ModifierComponentCombiner;

/**
 * TODO: Write class-level documentation.
 */
public class ActionModifierCombiner implements ModifierComponentCombiner<ActionModifier> {
    private static final ActionModifier NULL_MODIFIER = new ActionModifier() {
        private final String name = "null-action-modifier";

        @Override public String toString() {
            return name;
        }

        @Override public String name() {
            return name;
        }
    };

    private static final ActionModifier TEMPLATE = new ActionModifier() {
        @Override public String toString() {
            return "";
        }

        @Override public String name() {
            return "";
        }

        @Override public ActionModifier combine(ActionModifier next) {
            return next;
        }
    };

    @Override public ActionModifier combine(ActionModifier first, ActionModifier next) {
        return first.combine(next);
    }

    @Override public ActionModifier template() {
        return TEMPLATE;
    }

    @Override public ActionModifier nullModifier() {
        return NULL_MODIFIER;
    }
}
