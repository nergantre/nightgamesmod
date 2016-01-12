package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Stsflag;

public class Distracted extends Skill {

	public Distracted(Character self) {
		super("Distracted", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return false;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().distracted() && !getSelf().is(Stsflag.enthralled);
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(getSelf(), receive(c, 0, Result.normal, getSelf()));
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Distracted(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		return "You miss your opportunity to act.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character attacker) {
		return attacker.name()
				+ " looks a little unfocused and makes no attempt to defend herself.";
	}

	@Override
	public String describe(Combat c) {
		return "Caught off guard";
	}

}
