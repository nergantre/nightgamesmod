package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class SubRequirement implements CustomRequirement {
	@Override
	public boolean meets(Combat c, Character self, Character other) {
		if (c == null) {
			return false;
		}
		return c.getStance().sub(self);
	}
}
