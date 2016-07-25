package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if character self is prone in the current stance.
 */
public class ProneRequirement extends BaseRequirement {

    @Override public boolean meets(Combat c, Character self, Character other) {
        return c != null && c.getStance().prone(self);
    }
}
