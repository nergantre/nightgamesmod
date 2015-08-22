package nightgames.stance;


import nightgames.characters.Character;
import nightgames.characters.body.AnalPussyPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;

public abstract class FemdomSexStance extends Position {
	public FemdomSexStance(Character top, Character bottom, Stance stance) {
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
		
		if(!inserter.hasDick()){
			if(inserter.human()){
				c.write("With " + inserter.possessivePronoun() + " dick gone, you groan in frustration and cease your merciless movements.");
			} else {
				c.write(inserted.name() + " groans with frustration with the sudden disappearance of your dick.");
			}
			c.setStance(insert());
		}
		if (!inserted.hasPussy()) {
			if(inserted.human()){
				c.write("With your pussy suddenly disappearing, you can't continue riding " + inserter.name() + " anymore.");
			} else {
				c.write(inserted.name() + " groans with frustration with the sudden disappearance of her pussy.");
			}
			c.setStance(insert());
		}
	}
	
	@Override
	public abstract BodyPart topPart();
	@Override
	public abstract BodyPart bottomPart();
}
