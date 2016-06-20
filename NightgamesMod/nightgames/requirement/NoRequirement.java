package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Always returns true.
 */
public class NoRequirement implements Requirement {
    private static class Holder {
        private static final Requirement INSTANCE = new NoRequirement();
    }

    public static Requirement instance() {
        return Holder.INSTANCE;
    }

    @Override public String getKey() {
        return "none";
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return true;
    }
}
