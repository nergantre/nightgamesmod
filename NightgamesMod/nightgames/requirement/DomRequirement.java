package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if the combat's current stance puts character self in a dominant position.
 */
public class DomRequirement extends BaseRequirement {
    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return c != null && c.getStance().dom(self);
    }
}
