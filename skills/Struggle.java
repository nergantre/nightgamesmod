package skills;

import global.Global;
import stance.Cowgirl;
import stance.Doggy;
import stance.Missionary;
import stance.Neutral;
import stance.ReverseCowgirl;
import stance.Stance;
import stance.StandingOver;
import status.Bound;
import status.Braced;
import status.Stsflag;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Struggle extends Skill {

	public Struggle(Character self) {
		super("Struggle", self);
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		if (target.hasStatus(Stsflag.cockbound)) {
			return false;
		}
		if (self.hasStatus(Stsflag.cockbound)) {
			return true;
		}
		return ((!c.getStance().mobile(self) && !c.getStance().dom(self)) || self.bound())
				&& self.canRespond();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if (self.bound()) {
			Bound status = (Bound) target.getStatus(Stsflag.bound);
			if (self.check(Attribute.Power, 10 - self.escape())) {
				if (self.human()) {
					if (status != null) {
						c.write(self, "You manage to break free from the "
								+ status + ".");
					} else {
						c.write(self,
								"You manage to snap the restraints that are binding your hands.");
					}
				} else if (target.human()) {
					if (status != null) {
						c.write(self, self.name() + " slips free from the "
								+ status + ".");
					} else {
						c.write(self, self.name() + " breaks free.");
					}
				}
				self.free();
			} else {
				if (self.human()) {
					if (status != null) {
						c.write(self, "You struggle against the " + status
								+ ", but can't get free.");
					} else {
						c.write(self,
								"You struggle against your restraints, but can't get free.");
					}
				} else if (target.human()) {
					if (status != null) {
						c.write(self, self.name() + " struggles against the "
								+ status + ", but can't free her hands.");
					} else {
						c.write(self, self.name()
								+ " struggles, but can't free her hands.");
					}
				}
			}
		} else if (c.getStance().penetration(self) || c.getStance().penetration(target)) {
			if (c.getStance().enumerate() == Stance.anal) {
				if (self.check(
						Attribute.Power,
						20
								+ (target.getStamina().get() / 2 - self
										.getStamina().get() / 2)
								+ (target.get(Attribute.Power) - self
										.get(Attribute.Power))
								- ((10 * c.getStance().time) + self.escape()))) {
					if (self.human()) {
						c.write(self,
								"You manage to break away from "
										+ target.name() + ".");
					} else if (target.human()) {
						c.write(self,
								self.name()
										+ " pulls away from you and your dick slides out of her butt.");
					}
					if (!self.is(Stsflag.braced)) {
						self.add(new Braced(self));
					}
					c.setStance(new Neutral(self, target));
				} else {
					if (self.human()) {
						c.write(self,
								"You try to pull free, but " + target.name()
										+ " has a good grip on your waist.");
					} else if (target.human()) {
						c.write(self,
								self.name()
										+ " tries to squirm away, but you have better leverage.");
					}
				}
			} else {
				if (self.check(
						Attribute.Power,
						20
								+ (target.getStamina().get() / 2 - self
										.getStamina().get() / 2)
								+ (target.get(Attribute.Power) - self
										.get(Attribute.Power))
								- ((10 * c.getStance().time) + self.escape()))) {
					if (self.hasStatus(Stsflag.cockbound)) {
						c.write(self,
								Global.format(
										"With a strong pull, {self:subject} some how managed to wiggle out of {other:possessive} iron grip on {self:possessive} dick. "
												+ "However the sensations of {other:possessive} rough tongue sliding against {self:possessive} cockskin leaves {self:direct-object} gasping.",
										self, target));
						int m = 15;
						self.body.pleasure(target,
								target.body.getRandom("pussy"),
								self.body.getRandom("cock"), m, c);
						self.removeStatus(Stsflag.cockbound);
					}
					if (c.getStance().behind(self) && self.hasDick() && target.hasPussy()) {
						c.write(self,
								"You manage unbalance "
										+ target.name()
										+ " and push her forward onto her hands and knees. You follow her, still inside her tight wetness, and continue "
										+ "to fuck her from behind.");
						c.setStance(new Doggy(self, target));
					} else if (c.getStance().en == Stance.flying) {
						c.write(self,
								"You manage to shake yourself loose from the demoness.\n"
										+ "Immediatly afterwards you realize letting go of the person"
										+ " holding you a good distance up from the ground may not have been"
										+ " the smartest move you've ever made, as the ground is quickly"
										+ " approaching your face.");
						c.setStance(c.getStance().insert(self, self));
					} else if (c.getStance().prone(self) && self.hasDick() && target.hasPussy()) {
						c.write(self,
								"You surpise "
										+ target.name()
										+ " by hugging her close to your chest, preventing her from using stabilizing her position with her arms. You "
										+ "roll on top of her into traditional missionary position, careful not to let your cock slip out of her.");
						c.setStance(new Missionary(self, target));
					} else if (c.getStance().prone(self) && target.hasDick() && self.hasPussy()) {
						c.write(self,
								self.name()
										+ " wraps her legs around your waist and suddenly pulls you into a deep kiss. You're so surprised by this sneak attack that you "
										+ "don't even notice her roll you onto your back until you feel her weight on your hips. She moves her hips experimentally, enjoying the control "
										+ "she has in cowgirl position.");
						c.setStance(new Cowgirl(self, target));
					} else if (c.getStance().inserted(target)) {
						c.write(self,
								Global.format(
										"{self:SUBJECT-ACTION:manage|manages} to reach between {self:possessive} legs and grab hold of {other:possessive} ballsack, stopping {other:direct-object} in mid thrust. {self:SUBJECT-ACTION:smirk|smirks} at {other:direct-object} over {self:possessive} shoulder "
										+ "and pushes {self:possessive} butt against {other:direct-object}, using the leverage of "
										+ "{other:possessive} testicles to keep {other:direct-object} from backing away to maintain {self:possessive} balance. {self:SUBJECT-ACTION:force|forces} {other:direct-object} onto {other:possessive} back, while never breaking {other:possessive} connection. After "
										+ "some complex maneuvering, {other:subject-action:end|ends} up on the floor while {self:subject-action:straddle|straddles} {other:possessive} hips in a reverse cowgirl position.", self, target));
						c.setStance(new ReverseCowgirl(self, target));
					} else {
						c.write(self,
								Global.format("{self:SUBJECT-ACTION:manage|manages} to shake {other:direct-object} off." ,self,target));
						if (!self.is(Stsflag.braced)) {
							self.add(new Braced(self));
						}
						c.setStance(new Neutral(self, target));
					}
				} else {
					if (self.hasStatus(Stsflag.cockbound)) {
						c.write(self,
								Global.format(
										"{self:SUBJECT-ACTION:try|tries} to escape {other:possessive} iron grip on {self:possessive} dick. However, {other:possessive} pussy tongue has other ideas. {other:SUBJECT-ACTION:run|runs} {other:possessive} tongue up and down {self:possessive} cock and leaves {self:direct-object} gasping with pleasure.",
										self, target));
						self.body.pleasure(target,
								target.body.getRandom("pussy"),
								self.body.getRandom("cock"), 8, c);
					} else if (self.human()) {
						c.write(self,
								"You try to tip "
										+ target.name()
										+ " off balance, but she drops her hips firmly, pushing your cock deep inside her and pinning you to the floor.");
					} else if (target.human()) {
						if (c.getStance().behind(target)) {
							c.write(self,
									self.name()
											+ " struggles to gain a more dominant position, but with you behind her, holding her waist firmly, there is nothing she can do.");
						} else {
							c.write(self,
									self.name()
											+ " tries to roll on top of you, but you use you superior upper body strength to maintain your position.");
						}
					}
				}
			}
		} else {
			if (self.check(
					Attribute.Power,
					25
							+ (target.getStamina().get() / 2 - self
									.getStamina().get() / 2)
							+ (target.get(Attribute.Power) - self
									.get(Attribute.Power))
							- (10 * c.getStance().time + self.escape()))) {
				if (self.human()) {
					c.write(self,
							"You manage to scrabble out of " + target.name()
									+ "'s grip.");
				} else if (target.human()) {
					c.write(self, self.name() + " squirms out from under you.");
				} if (c.getStance().prone(self)) {
					c.setStance(new StandingOver(target, self));
				}
				c.setStance(new Neutral(self, target));
				if (!self.is(Stsflag.braced)) {
					self.add(new Braced(self));
				}
			} else {
				if (self.human()) {
					c.write(self,
							"You try to free yourself from "
									+ target.name()
									+ "'s grasp, but she has you pinned too well.");
				} else if (target.human()) {
					c.write(self,
							self.name()
									+ " struggles against you, but you maintain your position.");
				}
				target.weaken(c, 5 + Global.random(5) + self.get(Attribute.Power) / 2);
			}
		}
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Struggle(user);
	}

	public int speed() {
		return 0;
	}

	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return null;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		return null;
	}

	@Override
	public String describe() {
		return "Attempt to escape a submissive position using Power";
	}
}
