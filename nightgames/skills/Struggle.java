package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.CockPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Cowgirl;
import nightgames.stance.Doggy;
import nightgames.stance.Missionary;
import nightgames.stance.Neutral;
import nightgames.stance.ReverseCowgirl;
import nightgames.stance.Stance;
import nightgames.stance.StandingOver;
import nightgames.status.Bound;
import nightgames.status.Braced;
import nightgames.status.CockBound;
import nightgames.status.Falling;
import nightgames.status.Stsflag;

public class Struggle extends Skill {

	public Struggle(Character self) {
		super("Struggle", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		if (target.hasStatus(Stsflag.cockbound) || target.hasStatus(Stsflag.knotted)) {
			return false;
		}
		if (getSelf().hasStatus(Stsflag.cockbound) || getSelf().hasStatus(Stsflag.knotted)) {
			return true;
		}
		return ((!c.getStance().mobile(getSelf()) && !c.getStance().dom(getSelf())) || getSelf().bound())
				&& getSelf().canRespond();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().bound()) {
			Bound status = (Bound) target.getStatus(Stsflag.bound);
			if (getSelf().check(Attribute.Power, -getSelf().escape(c))) {
				if (getSelf().human()) {
					if (status != null) {
						c.write(getSelf(), "You manage to break free from the " + status + ".");
					} else {
						c.write(getSelf(), "You manage to snap the restraints that are binding your hands.");
					}
				} else if (target.human()) {
					if (status != null) {
						c.write(getSelf(), getSelf().name() + " slips free from the " + status + ".");
					} else {
						c.write(getSelf(), getSelf().name() + " breaks free.");
					}
				}
				getSelf().free();
			} else {
				if (getSelf().human()) {
					if (status != null) {
						c.write(getSelf(), "You struggle against the " + status + ", but can't get free.");
					} else {
						c.write(getSelf(), "You struggle against your restraints, but can't get free.");
					}
				} else if (target.human()) {
					if (status != null) {
						c.write(getSelf(), getSelf().name() + " struggles against the "
								+ status + ", but can't free her hands.");
					} else {
						c.write(getSelf(), getSelf().name()
								+ " struggles, but can't free her hands.");
					}
				}
				getSelf().struggle();
				return false;
			}
		} else if (c.getStance().penetration(getSelf()) || c.getStance().penetration(target)) {
			boolean knotted = getSelf().hasStatus(Stsflag.knotted);
			if (c.getStance().enumerate() == Stance.anal) {
				int diffMod = knotted ? 50 : 0;
				if (getSelf().check(Attribute.Power,
						(target.getStamina().get() / 2 - getSelf().getStamina().get() / 2)
								+ (target.get(Attribute.Power) - getSelf().get(Attribute.Power)) - (getSelf().escape(c))
								+ diffMod)) {
					if (getSelf().human()) {
						if (knotted) {
							c.write(getSelf(),
									"With a herculean effort, you painfully force " + target.possessivePronoun()
											+ " knot through your asshole, and the rest of her dick soon follows.");
							getSelf().removeStatus(Stsflag.knotted);
							getSelf().pain(c, 10);
						} else
							c.write(getSelf(), "You manage to break away from " + target.name() + ".");
					} else if (target.human()) {
						if (knotted) {
							c.write(getSelf(), getSelf().name()
									+ " roughly pulls away from you, groaning loudly as the knot in your dick pops free of her ass.");
							getSelf().removeStatus(Stsflag.knotted);
							getSelf().pain(c, 10);
						} else
							c.write(getSelf(),
									getSelf().name() + " pulls away from you and your dick slides out of her butt.");
					}
					c.setStance(new Neutral(getSelf(), target));
				} else {
					if (getSelf().human()) {
						if (knotted) {
							c.write(getSelf(), "You try to force " + target.possessivePronoun()
									+ " dick out of your ass, but the knot at its base is utterly unyielding.");
						} else
							c.write(getSelf(),
									"You try to pull free, but " + target.name() + " has a good grip on your waist.");
					} else if (target.human()) {
						if (knotted) {
							c.write(getSelf(),
									" frantically attempts to get your cock out of her ass, but your knot is keeping it inside her warm depths.");
						} else
							c.write(getSelf(),
									getSelf().name() + " tries to squirm away, but you have better leverage.");
					}
					getSelf().struggle();
					return false;
				}
			} else {
				int diffMod = 0;
				if (c.getStance().partFor(target) == CockPart.enlightened) {
					diffMod = 15;
				} else if (c.getStance().partFor(getSelf()) == CockPart.enlightened) {
					diffMod = -15;
				}
				if (getSelf().check(Attribute.Power, (target.getStamina().get() / 2 - getSelf().getStamina().get() / 2)
						+ (target.get(Attribute.Power) - getSelf().get(Attribute.Power)) - (getSelf().escape(c)) + diffMod)) {
					if (getSelf().hasStatus(Stsflag.cockbound)) {
						CockBound s = (CockBound) getSelf().getStatus(Stsflag.cockbound);
						c.write(getSelf(),
								Global.format(
										"With a strong pull, {self:subject} somehow managed to wiggle out of {other:possessive} iron grip on {self:possessive} dick. "
												+ "However the sensations of " + s.binding + " sliding against {self:possessive} cockskin leaves {self:direct-object} gasping.",
										getSelf(), target));
						int m = 15;
						getSelf().body.pleasure(target, target.body.getRandom("pussy"),
								getSelf().body.getRandom("cock"), m, c);
						getSelf().removeStatus(Stsflag.cockbound);
					}
					if (knotted) {
						c.write(getSelf(),
								Global.format(
										"{self:subject} somehow {self:SUBJECT-ACTION:manage|manages} to force {other:possessive} knot through {self:possessive} tight opening, stretching it painfully in the process.",
										getSelf(), target));
						getSelf().removeStatus(Stsflag.knotted);
						getSelf().pain(c, 10);
					}
					if ((c.getStance().behind(getSelf()) || c.getStance().en == Stance.reversecowgirl)
							&& c.getStance().inserted(getSelf())) {
						c.write(getSelf(),
								"You manage unbalance " + target.name()
										+ " and push her forward onto her hands and knees. You follow her, still inside her tight wetness, and continue "
										+ "to fuck her from behind.");
						c.setStance(new Doggy(getSelf(), target));
					} else if (c.getStance().en == Stance.flying) {
						c.write(getSelf(),
								"You manage to shake yourself loose from the demoness.\n"
										+ "Immediately afterwards you realize letting go of the person"
										+ " holding you a good distance up from the ground may not have been"
										+ " the smartest move you've ever made, as the ground is quickly"
										+ " approaching your face.");
						c.setStance(c.getStance().insert());
					} else if (c.getStance().inserted(getSelf())) {
						c.write(getSelf(), "You surpise " + target.name()
								+ " by hugging her close to your chest, preventing her from using stabilizing her position with her arms. You "
								+ "roll on top of her into traditional missionary position, careful not to let your cock slip out of her.");
						c.setStance(new Missionary(getSelf(), target));
					} else if (c.getStance().inserted(target) && c.getStance().en == Stance.doggy) {
						c.write(getSelf(),
								Global.format(
										"{self:SUBJECT-ACTION:manage|manages} to reach between {self:possessive} legs and grab hold of {other:possessive} ballsack, stopping {other:direct-object} in mid thrust. {self:SUBJECT-ACTION:smirk|smirks} at {other:direct-object} over {self:possessive} shoulder "
										+ "and pushes {self:possessive} butt against {other:direct-object}, using the leverage of "
										+ "{other:possessive} testicles to keep {other:direct-object} from backing away to maintain {self:possessive} balance. {self:SUBJECT-ACTION:force|forces} {other:direct-object} onto {other:possessive} back, while never breaking {other:possessive} connection. After "
										+ "some complex maneuvering, {other:subject-action:end|ends} up on the floor while {self:subject-action:straddle|straddles} {other:possessive} hips in a reverse cowgirl position.", getSelf(), target));
						c.setStance(new ReverseCowgirl(getSelf(), target));
					} else if (c.getStance().inserted(target)) {
						c.write(getSelf(),
								getSelf().name()
										+ " wraps her legs around your waist and suddenly pulls you into a deep kiss. You're so surprised by this sneak attack that you "
										+ "don't even notice her roll you onto your back until you feel her weight on your hips. She moves her hips experimentally, enjoying the control "
										+ "she has in cowgirl position.");
						c.setStance(new Cowgirl(getSelf(), target));
					} else {
						c.write(getSelf(),
								Global.format("{self:SUBJECT-ACTION:manage|manages} to shake {other:direct-object} off." ,getSelf(),target));
						c.setStance(new Neutral(getSelf(), target));
					}
				} else {
					if (getSelf().hasStatus(Stsflag.cockbound)) {
						CockBound s = (CockBound) getSelf().getStatus(Stsflag.cockbound);
						c.write(getSelf(),
								Global.format(
										"{self:SUBJECT-ACTION:try|tries} to escape {other:possessive} iron grip on {self:possessive} dick. However, {other:possessive} "
												+ s.binding
												+ " has other ideas. {other:SUBJECT-ACTION:run|runs} {other:possessive} "
												+ s.binding
												+ " up and down {self:possessive} cock and leaves {self:direct-object} gasping with pleasure.",
										getSelf(), target));
						getSelf().body.pleasure(target,
								target.body.getRandom("pussy"),
								getSelf().body.getRandom("cock"), 8, c);
					} else if (getSelf().human()) {
						if (c.getStance().inserted(getSelf())) {
							c.write(getSelf(), "You try to tip " + target.name()
									+ " off balance, but she drops her hips firmly, pushing your cock deep inside her and pinning you to the floor.");
						} else {
							if (knotted)
								c.write(getSelf(), "You struggle fruitlessly against the lump of "
										+ target.nameOrPossessivePronoun() + " knot.");
							else
								c.write(getSelf(), "You attempt to get away from " + target.name()
										+ ", but she drives her cock into you to the hilt, pinning you down.");
						}
					} else if (target.human()) {
						if (c.getStance().behind(target)) {
							c.write(getSelf(), getSelf().name()
									+ " struggles to gain a more dominant position, but with you behind her, holding her waist firmly, there is nothing she can do.");
						} else {
							c.write(getSelf(),
									getSelf().name()
											+ " tries to roll on top of you, but you use you superior upper body strength to maintain your position.");
						}
					}
					getSelf().struggle();
					return false;
				}
			}
		} else {
			if (getSelf().check(Attribute.Power, (target.getStamina().get() / 2 - getSelf().getStamina().get() / 2)
					+ (target.get(Attribute.Power) - getSelf().get(Attribute.Power)) - (getSelf().escape(c)))) {
				if (getSelf().human()) {
					c.write(getSelf(), "You manage to scrabble out of " + target.name() + "'s grip.");
				} else if (target.human()) {
					c.write(getSelf(), getSelf().name() + " squirms out from under you.");
				}
				c.setStance(new Neutral(getSelf(), target));
			} else {
				if (c.getStance().enumerate() == Stance.facesitting) {
					if (getSelf().human()) {
						c.write(getSelf(), "You try to free yourself from " + target.name()
								+ ", but she drops her ass over your face again, forcing you to service her.");
					} else if (target.human()) {
						c.write(getSelf(), getSelf().name()
								+ " struggles against you, but you drop your ass over her face again, forcing her to service you.");
					}
					if (target.hasPussy()) {
						(new Cunnilingus(getSelf())).resolve(c, target);
					} else {
						(new Anilingus(getSelf())).resolve(c, target);
					}
					target.weaken(c, 5 + Global.random(5) + getSelf().get(Attribute.Power) / 2);
					getSelf().struggle();
					return false;
				} else {
					if (getSelf().human()) {
						c.write(getSelf(), "You try to free yourself from " + target.name()
								+ "'s grasp, but she has you pinned too well.");
					} else if (target.human()) {
						c.write(getSelf(),
								getSelf().name() + " struggles against you, but you maintain your position.");
					}
					target.weaken(c, 5 + Global.random(5) + getSelf().get(Attribute.Power) / 2);
					getSelf().struggle();
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
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
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return null;
	}

	@Override
	public String describe() {
		return "Attempt to escape a submissive position using Power";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
