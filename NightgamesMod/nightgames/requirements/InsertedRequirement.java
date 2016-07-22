package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if character self is inserted into character other.
 */
public class InsertedRequirement extends BaseRequirement {

    public InsertedRequirement() {
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return c != null && c.getStance().inserted(self);
    }
}
