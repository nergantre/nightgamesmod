package nightgames.stance;


import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public abstract class MaledomSexStance extends Position {
	public MaledomSexStance(Character top, Character bottom, Stance stance) {
		super(top, bottom, stance);
	}

	@Override
	public float priorityMod(Character self) {
		float priority = 0;
		priority += getSubDomBonus(self, 4.0f);
		if (self.hasPussy()) { 
			priority += self.body.getRandomPussy().priority(self);
		}
		if (self.hasDick()) { 
			priority += self.body.getRandomCock().priority(self);
		}
		return priority;
	}

	@Override
	public void checkOngoing(Combat c) {
		Character inserter = inserted(top) ? top : bottom;
		Character inserted = inserted(top) ? bottom : top;
		
		if(!inserter.hasInsertable()){
			if(inserter.human()){
				c.write("With your " + inserter.body.getRandomInsertable() + " gone, you groan in frustration and cease your merciless movements.");
			} else {
				c.write(inserter.name() + " groans with frustration with the sudden disappearance of " + inserter.possessivePronoun() + " " + inserter.body.getRandomInsertable() + ".");
			}
			c.setStance(insertRandom());
		}
		if (!inserted.hasPussy()) {
			if(inserted.human()){
				c.write("With your pussy suddenly disappearing, " + inserter.subject() + " can't continue fucking you anymore.");
			} else {
				c.write("You groan with frustration with the sudden disappearance of " + inserted.nameOrPossessivePronoun() + " pussy.");
			}
			c.setStance(insertRandom());
		}
	}

	@Override
	public boolean penetration(Character c) {
		return c==top;
	}
	@Override
	public boolean inserted(Character c) {
		return c==top;
	}

	@Override
	public BodyPart topPart() {
		return top.body.getRandomInsertable();
	}

	@Override
	public BodyPart bottomPart() {
		return bottom.body.getRandomPussy();
	}
}
