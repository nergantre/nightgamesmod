package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;

public class Focus extends Skill {

	public Focus(Character self) {
		super("Focus", self, 2);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && !c.getStance().sub(getSelf());
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 25;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		getSelf().calm(c, Math.max(getSelf().getArousal().max() / 5, 20));
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Cunning) >= 15 && !user.has(Trait.undisciplined);
	}

	@Override
	public Skill copy(Character user) {
		return new Focus(user);
	}

	@Override
	public int speed() {
		return 0;
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.calming;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You take a moment to clear your thoughts, focusing your mind and calming your body.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()
				+ " closes her eyes and takes a deep breath. When she opens her eyes, she seems more composed.";
	}

	@Override
	public String describe(Combat c) {
		return "Calm yourself and gain some mojo";
	}
}
