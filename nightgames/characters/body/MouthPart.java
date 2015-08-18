package nightgames.characters.body;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.FluidAddiction;
import nightgames.status.Stsflag;
import nightgames.status.Trance;

import java.util.Map;
import java.util.Scanner;

public class MouthPart extends GenericBodyPart {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8842280103329414804L;
	public static MouthPart generic = new MouthPart("mouth", 0, 1, 1);
	public MouthPart(String desc, String descLong, double hotness, double pleasure, double sensitivity, boolean notable, String prefix) {
		super(desc, descLong, hotness, pleasure, sensitivity, notable, "mouth", prefix);
	}
	public MouthPart(String desc, double hotness, double pleasure,
			double sensitivity) {
		super(desc, hotness, pleasure, sensitivity, "mouth", "a ");
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (target.isErogenous() && opponent.has(Trait.lickable)) {
			c.write(opponent, Global.capitalizeFirstLetter(opponent.subjectAction("shudder", "shudders")) + " when licked by " + self.directObject() + ".");
			bonus += Global.random(4) + 5;
		}
		String fluid = target.getFluids(opponent);
		if (!fluid.isEmpty() && opponent.has(Trait.lacedjuices)) {
			c.write(self, Global.capitalizeFirstLetter(opponent.nameOrPossessivePronoun()) + " drug-laced " + fluid + " leaves " + self.nameOrPossessivePronoun() + " entire body tingling with arousal.");
			self.arouse(Math.max(opponent.getArousal().get() / 10, 5), c);
		}
		if (!fluid.isEmpty() && opponent.has(Trait.addictivefluids) && !self.is(Stsflag.tolerance)) {
			self.add(c, new FluidAddiction(self, opponent, 5));
			FluidAddiction st = (FluidAddiction) self.getStatus(Stsflag.fluidaddiction);
			if (st.activated()) {
				if (self.human()) {
					c.write(self, Global.capitalizeFirstLetter(Global.format("As {other:name-possessive} " + fluid + " flow down your throat, your entire mind fogs up. " +
							"You forget where you are, why you're here, and what you're doing. " +
							"The only thing left in you is an primal need to obtain more of {other:possessive} fluids.", self, opponent)));
				} else {
					c.write(self, Global.capitalizeFirstLetter(Global.format("As your " + fluid + " slides down {self:name-possessive} throat, you see a shadow pass over {self:possessive} face. " +
							"Whereas {self:name} was playfully teasing you just a few seconds ago, you can now only see a desperate need that {self:pronoun} did not possess before.", self, opponent)));
				}
			} else if (!st.isActive()) {
				if (self.human()) {
					c.write(self, Global.capitalizeFirstLetter(Global.format("You feel a strange desire to drink down more of {other:name-possessive} " + fluid + ".", self, opponent)));
				} else {
					c.write(self, Global.capitalizeFirstLetter(Global.format("{other:name} drinks down your " + fluid + " and seems to want more.", self, opponent)));
				}
			}
		}
		if (self.has(Trait.experttongue)) {
			if (Global.random(3) == 0 && self.canSpend(10) && !opponent.wary() && damage > 5) {
				if (!self.human()) {
					c.write(opponent, "<br>Your mind falls into a pink colored fog from the tongue lashing.");
				} else {
					c.write(opponent, "<br>" +opponent.name() + "'s mind falls into a pink colored fog from the tongue lashing.");
				}
				opponent.add(c, new Trance(opponent));
				self.spendMojo(c, 10);
			}
			bonus += Global.random(3) + 4;
		}
		if (self.has(Trait.soulsucker)) {
			if (!self.human()) {
				c.write(opponent, "<br>You feel faint as her lips touch your body, as if your will to fight is being sucked out through your " + target.describe(opponent) + " into her mouth.");
			} else {
				c.write(opponent, "<br>As your lips touch " + opponent.getName() + ", you instinctively draw in her spirit, forcing her energy through " + target.describe(opponent) + " into your mouth.");
			}
			bonus += Global.random(3) + 2;
			opponent.loseWillpower(c, Global.random(5) + 2);
			self.buildMojo(c, 15);
		}
		return bonus;
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		double pleasureMod = pleasure;
		pleasureMod += self.has(Trait.tongueTraining1) ? .5 : 0;
		pleasureMod += self.has(Trait.tongueTraining2) ? .7 : 0;
		pleasureMod += self.has(Trait.tongueTraining3) ? .9 : 0;
		return pleasureMod;
	}

	@Override
	public String getFluids(Character c) {
		return "saliva";
	}

	@Override
	public BodyPart loadFromDict(Map<String,Object> dict) {
		try {
		GenericBodyPart part = new MouthPart(
									(String)dict.get("desc"),
									(Double)dict.get("hotness"),
									(Double)dict.get("pleasure"),
									(Double)dict.get("sensitivity"));
			return part;
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean isErogenous() {
		return false;
	}
	@Override
	public boolean isVisible(Character c) {
		return true;
	}
}
