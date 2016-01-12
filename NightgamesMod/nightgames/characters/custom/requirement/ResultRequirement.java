package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;

public class ResultRequirement implements CustomRequirement {
	Result result;

	public ResultRequirement(Result result) {
		this.result = result;
	}

	@Override
	public boolean meets(Combat c, Character self, Character other) {
		if (c == null) {
			return false;
		}
		return c.state == result;
	}
}
