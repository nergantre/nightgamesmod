package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if character self's level is at least the specified amount.
 */
public class LevelRequirement implements Requirement {
    private final int level;

    public LevelRequirement(int level) {
        this.level = level;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.getLevel() >= level;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        LevelRequirement that = (LevelRequirement) o;

        return level == that.level;

    }

    @Override public int hashCode() {
        return level;
    }
}
