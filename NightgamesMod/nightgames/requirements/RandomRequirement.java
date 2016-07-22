package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

/**
 * Returns true based on a random variable between 0.0 and 1.0, with probability equal to the threshold.
 */
public class RandomRequirement extends BaseRequirement {
    private final float threshold;

    public RandomRequirement(float threshold) {
        if (threshold < 0f) {
            this.threshold = 0f;
        } else if (threshold > 1f) {
            this.threshold = 1f;
        } else {
            this.threshold = threshold;
        }
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return Global.randomfloat() < threshold;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        RandomRequirement that = (RandomRequirement) o;

        return Math.abs(this.threshold - that.threshold) < 1e-6;

    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Long.valueOf(Math.round(threshold * 1e6)).intValue();
        return result;
    }
}
