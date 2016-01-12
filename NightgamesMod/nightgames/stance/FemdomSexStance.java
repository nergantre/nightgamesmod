package nightgames.stance;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
		
		if(!inserter.hasInsertable()){
			if(inserter.human()){
				c.write(inserted.name() + " groans with frustration with the sudden disappearance of your pole.");
			} else {
				c.write("With " + inserter.nameOrPossessivePronoun() + " phallus gone, you groan in frustration and cease your merciless riding.");
			}
			c.setStance(insertRandom());
		}
		if (!inserted.hasPussy()) {
			if(inserted.human()){
				c.write("With your pussy suddenly disappearing, you can't continue riding " + inserter.name() + " anymore.");
			} else {
				c.write(inserted.name() + " groans with frustration with the sudden disappearance of her pussy.");
			}
			c.setStance(insertRandom());
		}
	}

	@Override
	public boolean inserted(Character c) {
		return c==bottom;
	}
	@Override
	public List<BodyPart> topParts() {
		return Arrays.asList(top.body.getRandomPussy()).stream().filter(part -> part != null && part.present()).collect(Collectors.toList());
	}

	@Override
	public List<BodyPart> bottomParts() {
		return Arrays.asList(bottom.body.getRandomInsertable()).stream().filter(part -> part != null && part.present()).collect(Collectors.toList());
	}

	public double pheromoneMod (Character self) {
		return 3;
	}
}
