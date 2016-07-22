package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 *
 */
class FalseRequirement extends BaseRequirement {
    @Override public boolean meets(Combat c, Character self, Character other) {
        return false;
    }
}
