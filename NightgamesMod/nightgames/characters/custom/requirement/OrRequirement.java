package nightgames.characters.custom.requirement;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class OrRequirement implements CustomRequirement {
    private List<List<CustomRequirement>> reqs;

    public OrRequirement(List<List<CustomRequirement>> reqs) {
        this.reqs = reqs;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return reqs.stream().anyMatch(subs -> subs.stream().allMatch(r -> r.meets(c, self, other)));
    }
}
