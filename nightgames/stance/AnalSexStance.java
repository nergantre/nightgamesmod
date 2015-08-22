package nightgames.stance;


import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.AnalPussyPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;

public abstract class AnalSexStance extends Position {
	public AnalSexStance(Character top, Character bottom, Stance stance) {
		super(top, bottom, stance);
	}

	@Override
	public float priorityMod(Character self) {
		float priority = 0;
		priority += getSubDomBonus(self, 4);
		if (!inserted(self) && self.body.getRandom("ass") != null) {
			priority += self.body.getRandom("ass").priority(self);
		} else if (inserted(self) && self.body.getRandomInsertable() != null){
			priority += self.body.getRandomInsertable().priority(self);
		}
		return priority;
	}
	
	@Override
	public abstract BodyPart topPart();
	@Override
	public abstract BodyPart bottomPart();
}
