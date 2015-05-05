package stance;


import combat.Combat;

import characters.Character;
import characters.body.AnalPussyPart;
import characters.body.PussyPart;

public abstract class AnalSexStance extends Position {
	public AnalSexStance(Character top, Character bottom, Stance stance) {
		super(top, bottom, stance);
	}

	@Override
	public float priorityMod(Character self) {
		float priority = 0;
		if (dom(self)) {
			priority += 4;
		}
		if (!inserted(self) && self.body.getRandom("ass") != null) {
			priority += self.body.getRandom("ass").priority(self);
		} else if (inserted(self) && self.body.getRandomInsertable() != null){
			priority += self.body.getRandomInsertable().priority(self);
		}
		return priority;
	}
}
