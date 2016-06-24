package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class RandomRequirement implements CustomRequirement {
    float thresh;

    public RandomRequirement(float thresh) {
        this.thresh = thresh;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return Global.randomfloat() < thresh;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RandomRequirement that = (RandomRequirement) o;

        return Float.compare(that.thresh, thresh) == 0;

    }

    @Override public int hashCode() {
        return (thresh != +0.0f ? Float.floatToIntBits(thresh) : 0);
    }
}
