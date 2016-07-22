package nightgames.modifier.status;

import nightgames.characters.Character;
import nightgames.modifier.ModifierCategory;
import nightgames.modifier.ModifierComponent;
import nightgames.status.Status;

public class StatusModifier implements ModifierCategory<StatusModifier>, ModifierComponent {
    public static final StatusModifierCombiner combiner = new StatusModifierCombiner();

    private final Status status;
    private final boolean playerOnly;

    public StatusModifier(Status status, boolean playerOnly) {
        this.status = status;
        this.playerOnly = playerOnly;
    }

    public StatusModifier(Status status) {
        this(status, false);
    }

    protected StatusModifier() {
        status = null;
        playerOnly = true;
    }

    public void apply(Character c) {
        if ((!playerOnly || c.human()) && status != null) {
            c.add(status.instance(c, null));
        }
    }

    public StatusModifier combine(StatusModifier next) {
        StatusModifier first = this;
        return new StatusModifier() {
            @Override
            public void apply(Character c) {
                first.apply(c);
                next.apply(c);
            }

            @Override public String toString() {
                return first.toString() + " and " + next.toString();
            }
        };
    }

    @Override
    public String toString() {
        return status.name;
    }

    @Override public String name() {
        return "status-modifier-" + status.name;
    }
}
