package nightgames.characters.custom.requirement;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class AndRequirement implements CustomRequirement {
	private List<List<CustomRequirement>> reqs;

	public AndRequirement(List<List<CustomRequirement>> reqs) {
		this.reqs = reqs;
	}

	@Override
	public boolean meets(Combat c, Character self, Character other) {
		return reqs.stream().allMatch(subs -> subs.stream().allMatch(r -> r.meets(c, self, other)));
	}
}
