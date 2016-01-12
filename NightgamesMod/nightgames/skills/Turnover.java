package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Behind;
import nightgames.stance.Stance;

public class Turnover extends Skill {

	public Turnover(Character self) {
		super("Turn Over", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Power) >= 4;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && c.getStance().enumerate() == Stance.standingover && c.getStance().dom(getSelf());
	}

	@Override
	public String describe(Combat c) {
		return "Turn your opponent over and get behind her";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		c.setStance(new Behind(getSelf(), target));
		target.emote(Emotion.dominant, 20);
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Turnover(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You turn " + target.name() + " onto her hands and knees. You move behind her while she slowly gets up.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()
				+ " rolls you onto your stomach. You push yourself back up, but she takes the opportunity to get behind you.";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
