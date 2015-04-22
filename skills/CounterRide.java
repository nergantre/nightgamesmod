package skills;

import stance.Cowgirl;
import stance.Missionary;
import combat.Combat;
import combat.Result;

import global.Global;
import characters.Attribute;
import characters.Character;

public class CounterRide extends CounterBase {
	public CounterRide(Character self) {
		super("Invitation", self, 5, Global.format("{self:SUBJECT-ACTION:invite|invites} you inside her.", self, self));
	}

	@Override
	public float priorityMod(Combat c) {
		return Global.randomfloat() * 2;
	}

	@Override
	public void resolveCounter(Combat c, Character target) {
		if (self.human()) {
			c.write(self, deal(c, 0, Result.normal, target));
		} else {
			c.write(self, receive(c, 0, Result.normal, target));
		}
		if (target.hasDick() && self.hasPussy()) {
			c.setStance(new Cowgirl(self, target));
			target.body.pleasure(self, self.body.getRandomPussy(), target.body.getRandomCock(), 15, c);
		} else {
			c.setStance(new Missionary(self, target));
			target.body.pleasure(self, self.body.getRandomCock(), target.body.getRandomPussy(), 15, c);
		}
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Seduction) > 15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !c.getStance().dom(self) && !c.getStance().dom(target) && self.canAct()
				&& self.pantsless() && target.pantsless()
				&&((self.hasDick() && target.hasPussy()) || (self.hasPussy() && target.hasDick()))
				&&self.canSpend(getMojoCost());
	}

	public int getMojoCost() {
		return 15;
	}

	@Override
	public String describe() {
		return "Invites opponent into your embrace";
	}

	@Override
	public Skill copy(Character user) {
		return new CounterRide(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.setup && self.hasPussy()) {
			return Global.format("You turn around and bend over with your ass seductively waving in the air. You slowly " +
					"tease your glistening lower lips and spread them apart, inviting {other:name} to take {other:possessive} pleasure.", self, target);
		} else if (modifier == Result.setup && self.hasDick()) {
			return Global.format("You grab your cock and quickly stroke it to full mast. You let your dick go and it swings back and forth, catching {other:name-possessive} gaze.", self, target);
		} else if (self.hasPussy() && target.hasDick()) {
			return Global.format("As {other:subject} approaches you, you suddenly lower your center of balance and sweep {other:possessive} legs out from under her. " +
					"With one smooth motion, you drop your hips and lodge {other:possessive} dick firmly inside yourself.", self, target);
		} else {
			return Global.format("As {other:subject} approaches you, you suddenly lower your center of balance and sweep {other:possessive} legs out from under her. " +
					"With one smooth motion, you spread her legs apart and plunge into her depths.", self, target);
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.setup && self.hasPussy()) {
			return Global.format("{self:SUBJECT} turns around and bends over her ass seductively waving in the air. She slowly " +
					"teases her glistening lower lips and spread them apart, inviting you in to her embrace.", self, target);
		} else if (modifier == Result.setup && self.hasDick()) {
			return Global.format("{self:SUBJECT} takes out her cock and strokes it to full mast. She then lets her dick go and it swings back and forth, catching your gaze.", self, target);
		} else if (self.hasPussy() && target.hasDick()) {
			return Global.format("As {other:subject} approaches {self:name}, she suddenly disappears from your view; half a second later, your legs are swept out from under you. " +
					"With a soft giggle, {self:name} swiftly mounts you and starts riding your cock.", self, target);
		} else {
			return Global.format("As {other:subject} approaches {self:name}, she suddenly disappears from your view; half a second later, your legs are swept out from under you. " +
					"With a sexy grin, {self:name} wrenches your legs apart and plunges into your slobbering vagina.", self, target);
		}
	}
}
