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

    @Override public String getKey() {
        return "level";
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.getLevel() >= level;
    }
}
