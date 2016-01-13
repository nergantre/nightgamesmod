package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class AnalRequirement implements CustomRequirement {

    private final boolean anal;

    public AnalRequirement(boolean anal) {
        this.anal = anal;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        if (c == null)
            return false;
        return c.getStance().anallyPenetrated() == anal;
    }

}
