package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if self is anally penetrated.
 */
public class AnalRequirement implements Requirement {

    public AnalRequirement() {
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return c != null && c.getStance().anallyPenetrated(self);
    }

}
