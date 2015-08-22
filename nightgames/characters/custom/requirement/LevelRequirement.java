package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class LevelRequirement implements CustomRequirement {
	int level;

	public LevelRequirement(int level) {
		this.level = level;
	}

	@Override
	public boolean meets(Combat c, Character self, Character other) {
		return self.getLevel() >= (level);
	}
}
