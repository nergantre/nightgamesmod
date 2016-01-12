package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Mount;
import nightgames.stance.Stance;

public class Blindside extends Skill {

	public Blindside(Character self) {
		super("Blindside", self, 2);
	}

	@Override
	public int getMojoCost(Combat c) {
		return 15;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.has(Trait.temptress) && user.get(Attribute.Technique) >= 10;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().enumerate() == Stance.neutral;
	}

	@Override
	public String describe(Combat c) {
		return "Distract your opponent and take them down.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(getSelf(),
					String.format(
							"You move up to %s and kiss %s strongly. "
									+ "While %s is distracted, you throw %s down and plant "
									+ "yourself on top of %s.",
							target.name(), target.pronoun(), target.pronoun(),
							target.directObject(), target.directObject()));
		} else {
			c.write(getSelf(), "Seductively swaying her hips, " + getSelf().subject()
					+ " shashays over to you. "
					+ "Her eyes fix you in place as she leans in and firmly kisses you, shoving her tongue down"
					+ " your mouth. You are so absorbed in kissing back, that you only notice her ulterior motive"
					+ " once she has already swept your legs out from under you and she has landed on top of you.");
		}
		c.setStance(new Mount(getSelf(), target));
		getSelf().emote(Emotion.confident, 15);
		getSelf().emote(Emotion.dominant, 15);
		target.emote(Emotion.nervous,10);
		return false;
	}

	@Override
	public Skill copy(Character user) {
		return new Blindside(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		return null;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		return null;
	}

}
