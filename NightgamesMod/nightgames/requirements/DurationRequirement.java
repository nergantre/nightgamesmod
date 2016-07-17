package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true while duration > 0. Primarily used when keeping track of time-limited statuses.
 */
public class DurationRequirement extends BaseRequirement {
    // duration is constrained to being a positive integer
    private int duration;

    public DurationRequirement(int duration) {
        this.duration = duration;
    }

    public void tick() {
        tick(1);
    }

    public void tick(int i) {
        duration = Math.max(0, duration - i);
    }

    public int remaining() {
        return duration;
    }

    public void reset(int i) {
        duration = Math.max(0, i);
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
