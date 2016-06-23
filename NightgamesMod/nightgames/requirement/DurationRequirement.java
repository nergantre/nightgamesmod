package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true while duration > 0. Primarily used when keeping track of time-limited statuses.
 */
public class DurationRequirement implements Requirement {
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
}
