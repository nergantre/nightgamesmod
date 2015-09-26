package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class DurationRequirement implements CustomRequirement {
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