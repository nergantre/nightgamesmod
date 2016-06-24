package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class EitherInsertedRequirement implements CustomRequirement {
    boolean inserted;

    public EitherInsertedRequirement(boolean inserted) {
        this.inserted = inserted;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        if (c == null) {
            return false;
        }
        return c.getStance().inserted() == inserted;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        EitherInsertedRequirement that = (EitherInsertedRequirement) o;

        return inserted == that.inserted;

    }

    @Override public int hashCode() {
        return (inserted ? 1 : 0);
    }
}
