package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if character self is prone in the current stance.
 */
public class ProneRequirement implements Requirement {

    @Override public String getKey() {
        return "prone";
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return c != null && c.getStance().prone(self);
    }
}
