package nightgames.modifier.status;

import nightgames.modifier.ModifierComponentCombiner;

/**
 * TODO: Write class-level documentation.
 */
public class StatusModifierCombiner implements ModifierComponentCombiner<StatusModifier> {
    private static final StatusModifier NULL_MODIFIER = new StatusModifier() {
        @Override public String toString() {
            return "null-status-modifier";
        }
    };

    private static final StatusModifier TEMPLATE = new StatusModifier() {
        @Override public String toString() {
            return "";
        }

        @Override public String name() {
            return "";
        }

        @Override public StatusModifier combine(StatusModifier next) {
            return next;
        }
    };

    @Override public StatusModifier combine(StatusModifier first, StatusModifier next) {
        return first.combine(next);
    }

    @Override public StatusModifier template() {
        return null;
    }

    @Override public StatusModifier nullModifier() {
        return NULL_MODIFIER;
    }
}
