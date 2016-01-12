package nightgames.characters.custom.requirement;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class NotRequirement implements CustomRequirement {
	private List<CustomRequirement> req;

	public NotRequirement(List<CustomRequirement> req) {
		this.req = req;
	}

	@Override
	public boolean meets(Combat c, Character self, Character other) {
		return !req.stream().allMatch(r -> r.meets(c, self, other));
	}
}
