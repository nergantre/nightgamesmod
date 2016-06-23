package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 *
 */
public class TrueRequirement implements Requirement {
    @Override public boolean meets(Combat c, Character self, Character other) {
        return true;
    }
}
