package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.body.CockPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Knotted;
import nightgames.status.Stsflag;

public class ToggleKnot extends Skill {

	public ToggleKnot(Character self) {
		super("Toggle Knot", self);
	}

	private boolean isActive(Character target) {
		return target.hasStatus(Stsflag.knotted);
	}

	@Override
	public boolean requirements(Character user) {
		return user.body.get("cock").contains(CockPart.primal);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return isActive(target) || getSelf().canAct() && c.getStance().inserted(getSelf());
	}

	@Override
	public String describe() {
		return "Inflate or deflate your knot.";
	}

	@Override
	public String getLabel(Combat c) {
		if (isActive(c.getOther(getSelf())))
			return "Deflate Knot";
		return "Inflate Knot";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (isActive(target)) {
			if (getSelf().human()) {
				c.write(getSelf(),
						"Deciding she's had enough for now, you let your cock return to its regular shape, once again permitting movement.");
			} else if (target.human()) {
				String part = c.getStance().partFor(target).describe(target);
				c.write(getSelf(), "You feel the intense pressure in your " + part + " recede as " + target.name()
						+ " allows her knot to deflate.");
			}
			target.removeStatus(Stsflag.knotted);
		} else {
			if (getSelf().human()) {
				c.write(getSelf(),
						"You'd like to stay inside " + target.name() + " for a bit, so you "
								+ (c.getStance().canthrust(getSelf()) ? "thrust" : "buck up")
								+ " as deep inside of her as you can and send a mental command to the base of your cock, where your"
								+ " knot soon swells up, locking you inside,");
			} else if (target.human()) {
				String firstPart;
				if (c.getStance().dom(getSelf())) {
					firstPart = getSelf().name() + " bottoms out inside of you, and something quickly feels off.";
				} else {
					firstPart = getSelf().name()
							+ " pulls you all the way onto her cock. As soon as your pelvis touches hers, something starts happening.";
				}
				c.write(getSelf(),
						firstPart
								+ " A ball swells up at the base of her dick, growing to the size of a small apple. You're not"
								+ " getting it out of you any time soon...");
			}
			target.add(c, new Knotted(target, getSelf(), c.getStance().analinserted()));
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new ToggleKnot(user);
	}

	@Override
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

}
