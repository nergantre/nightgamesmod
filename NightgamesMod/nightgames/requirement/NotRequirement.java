package nightgames.requirement;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if the sub-requirement is NOT met.
 */
public class NotRequirement implements Requirement {
    private final Requirement req;

    public NotRequirement(Requirement req) {
        this.req = req;
    }

    @Override public String getKey() {
        return "not";
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return !req.meets(c, self, other);
    }
}
