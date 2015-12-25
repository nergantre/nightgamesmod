package nightgames.characters.custom.requirement;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class ReverseRequirement implements CustomRequirement {
	private List<CustomRequirement> req;

	public ReverseRequirement(List<CustomRequirement> req) {
		this.req = req;
	}

	@Override
	public boolean meets(Combat c, Character self, Character other) {
		if (other == null) {
			return false;
		}
		return req.stream().allMatch(r -> r.meets(c, other, self));
	}
}
