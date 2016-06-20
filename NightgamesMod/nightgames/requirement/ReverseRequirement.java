package nightgames.requirement;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if the sub-requirement returns true with self and other swapped.
 */
public class ReverseRequirement implements Requirement {
    private final Requirement req;

    public ReverseRequirement(Requirement req) {
        this.req = req;
    }

    @Override public String getKey() {
        return "reverse";
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return other != null && req.meets(c, other, self);
    }
}
