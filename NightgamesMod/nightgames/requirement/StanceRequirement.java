package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class StanceRequirement implements Requirement {
    private final String stance;

    public StanceRequirement(String stance) {
        this.stance = stance;
    }

    @Override public String getKey() {
        return "stance";
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return c != null && c.getStance().getClass().getSimpleName().equals(stance);
    }
}
