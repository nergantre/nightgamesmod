package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Created by Ryplinn on 6/17/2016.
 */
public class FalseRequirement implements Requirement {
    @Override public boolean meets(Combat c, Character self, Character other) {
        return false;
    }

    @Override public String getKey() {
        return "false";
    }
}
