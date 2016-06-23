package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 *
 */
public class FalseRequirement implements Requirement {
    @Override public boolean meets(Combat c, Character self, Character other) {
        return false;
    }
}
