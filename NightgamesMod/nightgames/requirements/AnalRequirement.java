package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if self is anally penetrated.
 */
public class AnalRequirement extends BaseRequirement {

    public AnalRequirement() {
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return c != null && c.getStance().anallyPenetrated(c, self);
    }

}
