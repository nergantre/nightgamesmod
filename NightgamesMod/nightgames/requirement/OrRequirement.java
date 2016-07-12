package nightgames.requirement;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if at least one sub-requirement is met.
 */
public class OrRequirement extends BaseRequirement {
    private final List<Requirement> reqs;

    public OrRequirement(List<Requirement> reqs) {
        this.reqs = reqs;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return reqs.stream().anyMatch(r -> r.meets(c, self, other));
    }
}
