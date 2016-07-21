package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if the sub-requirements returns true with self and other swapped.
 */
public class ReverseRequirement extends BaseRequirement {
    private final Requirement req;

    public ReverseRequirement(Requirement req) {
        this.req = req;
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return other != null && req.meets(c, other, self);
    }
}
