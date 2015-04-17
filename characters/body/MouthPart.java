package characters.body;

import global.Global;

import java.util.Scanner;

import status.Trance;

import combat.Combat;

import characters.Character;
import characters.Trait;

public class MouthPart extends GenericBodyPart {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8842280103329414804L;
	public static MouthPart generic = new MouthPart("mouth", 0, 1, 1);

	public MouthPart(String desc, double hotness, double pleasure,
			double sensitivity) {
		super(desc, hotness, pleasure, sensitivity, "mouth");
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		if (target.isErogenous() && opponent.has(Trait.lickable)) {
			c.write(opponent, Global.capitalizeFirstLetter(opponent.subjectAction("shudder", "shudders")) + " when licked by " + self.directObject() + ".");
			damage = damage * 1.5;
		}
		String fluid = target.getFluids(opponent);
		if (!fluid.isEmpty() && opponent.has(Trait.lacedjuices)) {
			c.write(self, Global.capitalizeFirstLetter(opponent.nameOrPossessivePronoun()) + " drug-laced " + fluid + " leaves " + self.nameOrPossessivePronoun() + " entire body tingling with arousal.");
			self.arouse(Math.max(opponent.getArousal().get() / 10, 5), c);
		}
		if (self.has(Trait.greatkiss) && Global.random(3) == 0 && self.canSpend(10) && !opponent.wary() && damage > 5) {
			if (!self.human()) {
				c.write(opponent, "<br>Your mind falls into a pink colored fog from the tongue lashing.");
			} else {
				c.write(opponent, "<br>" +opponent.name() + "'s mind falls into a pink colored fog from the tongue lashing.");
			}
			opponent.add(new Trance(opponent));
			self.spendMojo(10);
		}
		return damage;
	}

	@Override
	public String getFluids(Character c) {
		return "saliva";
	}

	public static BodyPart load(Scanner loader) {
		String line = loader.nextLine();
		String vals[] = line.split(",");
		assert(vals.length >= 6);
		return new MouthPart(vals[0].trim(), Double.valueOf(vals[2].trim()), Double.valueOf(vals[3].trim()), Double.valueOf(vals[4].trim()));
	}
	@Override
	public boolean isErogenous() {
		return false;
	}
}
