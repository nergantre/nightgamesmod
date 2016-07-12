package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class SubRequirement extends BaseRequirement {
    @Override public boolean meets(Combat c, Character self, Character other) {
        return c != null && c.getStance().sub(self);
    }
}
