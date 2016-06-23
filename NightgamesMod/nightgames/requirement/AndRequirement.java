package nightgames.requirement;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if all sub-requirements are met.
 */
public class AndRequirement implements Requirement {
    private final List<Requirement> reqs;

    public AndRequirement(List<Requirement> reqs) {
        this.reqs = reqs;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return reqs.stream().allMatch(r -> r.meets(c, self, other));
    }
}
