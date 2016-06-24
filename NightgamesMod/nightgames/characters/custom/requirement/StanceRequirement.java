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

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        StanceRequirement that = (StanceRequirement) o;

        return stance.equals(that.stance);

    }

    @Override public int hashCode() {
        return stance.hashCode();
    }
}
