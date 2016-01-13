package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class StanceRequirement implements CustomRequirement {
    String stance;

    public StanceRequirement(String stance) {
        this.stance = stance;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        if (c == null) {
            return false;
        }
        return c.getStance().getClass().getSimpleName().equals(stance);
    }
}
