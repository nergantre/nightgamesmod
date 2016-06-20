package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

/**
 * Returns true based on a random variable between 0.0 and 1.0, with probability equal to the threshold.
 */
public class RandomRequirement implements Requirement {
    private final float threshold;

    public RandomRequirement(float threshold) {
        this.threshold = threshold;
    }

    @Override public String getKey() {
        return "random";
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return Global.randomfloat() < threshold;
    }
}
