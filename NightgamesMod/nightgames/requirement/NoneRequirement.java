package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Always returns true.
 */
public class NoneRequirement extends BaseRequirement {
    private static class Holder {
        private static final Requirement INSTANCE = new NoneRequirement();
    }

    public static Requirement instance() {
        return Holder.INSTANCE;
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return true;
    }
}

