package skills;

import java.util.List;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;
import characters.body.BodyPart;
import characters.body.BreastsPart;

import combat.Combat;
import combat.Result;

public class Paizuri extends Skill {

	public Paizuri(Character self) {
		super("Use Breasts", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Seduction) >= 28
				&& !self.has(Trait.petite) && self.hasBreasts();
	}
	static int MIN_REQUIRED_BREAST_SIZE = 1;

	@Override
	public boolean usable(Combat c, Character target) {
		return self.hasBreasts() && self.body.getLargestBreasts().size > MIN_REQUIRED_BREAST_SIZE && target.hasDick() && self.topless()
				&& target.pantsless() && c.getStance().reachBottom(self)
				&& !c.getStance().behind(self) && !c.getStance().behind(target)
				&& self.canAct() && !c.getStance().penetration(self);
	}

	@Override
	public void resolve(Combat c, Character target) {
		BreastsPart breasts = self.body.getLargestBreasts();
		//try to find a set of breasts large enough, if none, default to largest.
		for (int i = 0 ; i < 3; i++) {
			BreastsPart otherbreasts = self.body.getRandomBreasts();
			if (otherbreasts.size > MIN_REQUIRED_BREAST_SIZE) {
				breasts = otherbreasts;
				break;
			}
		}

		int sizeMod = Math.max(1, breasts.size - 2);
		int m = sizeMod * (2 + Global.random(3));
		if (target.human()) {
			c.write(self, receive(0, Result.normal, target, breasts));
		}
		target.body.pleasure(self, self.body.getRandom("breasts"), target.body.getRandom("cock"), m, c);					

		self.buildMojo(c, 25);
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Seduction) >= 28
				&& !user.has(Trait.petite) && user.hasBreasts();
	}

	@Override
	public Skill copy(Character user) {
		return new Paizuri(user);
	}

	public int speed() {
		return 4;
	}

	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}

	public String receive(int damage, Result modifier, Character target, BreastsPart breasts) {
		StringBuilder b = new StringBuilder();
		b.append(self.name() + " squeezes your dick between her ");
		b.append(breasts.describe(self));
		b.append(". She rubs them up and down your shaft and teasingly licks your tip.");
		return b.toString();
	}

	@Override
	public String describe() {
		return "Rub your opponent's dick between your boobs";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
