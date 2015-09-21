package nightgames.stance;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
	public List<BodyPart> topParts() {
		return Arrays.asList(top.body.getRandomInsertable()).stream().filter(part -> part != null && part.present()).collect(Collectors.toList());
	}

	@Override
	public List<BodyPart> bottomParts() {
		return Arrays.asList(bottom.body.getRandomAss()).stream().filter(part -> part != null && part.present()).collect(Collectors.toList());
	}

	public double pheromoneMod (Character self) {
		return 3;
	}
}
