package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class NoRequirement implements CustomRequirement {
    private static class Holder {
        private static final CustomRequirement INSTANCE = new NoRequirement();
    }
    public static CustomRequirement instance() {
        return Holder.INSTANCE;
    }
    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return true;
    }
}
