package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class PositionRequirement extends BaseRequirement {
    private final String position;

    public PositionRequirement(String position) {
        this.position = position;
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return c != null && c.getStance().getClass().getSimpleName().equals(position);
    }
}
