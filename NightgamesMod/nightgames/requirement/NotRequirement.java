package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if the sub-requirement is NOT met.
 */
public class NotRequirement extends BaseRequirement {
    private final Requirement req;

    public NotRequirement(Requirement req) {
        this.req = req;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return !req.meets(c, self, other);
    }
}
