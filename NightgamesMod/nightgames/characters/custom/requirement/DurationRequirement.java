package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class DurationRequirement implements CustomRequirement {
    public int duration;

    public DurationRequirement(int duration) {
        this.duration = duration;
    }

    public void tick(int i) {
        duration -= i;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return duration > 0;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DurationRequirement that = (DurationRequirement) o;

        return duration == that.duration;

    }

    @Override public int hashCode() {
        return duration;
    }
}
