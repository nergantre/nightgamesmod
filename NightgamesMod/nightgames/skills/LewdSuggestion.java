package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Horny;
import nightgames.status.Stsflag;

public class LewdSuggestion extends Skill {

	public LewdSuggestion(Character self) {
		super("Lewd Suggestion", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return getSelf().getPure(Attribute.Hypnosis) >= 3;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && c.getStance().mobile(getSelf())
				&& !c.getStance().behind(getSelf())
				&& !c.getStance().behind(target)
				&& !c.getStance().sub(getSelf()) && target.is(Stsflag.charmed);
	}

	@Override
	public String describe(Combat c) {
		return "Plant an erotic suggestion in your hypnotized target.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (target.is(Stsflag.horny)) {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.strong, target));
			} else {
				c.write(getSelf(), receive(c, 0, Result.strong, target));
			}
		} else if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}

		target.add(new Horny(target, 10, 4, "Hypnosis"));
		target.emote(Emotion.horny, 30);
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new LewdSuggestion(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.strong) {
			return String.format(
					"You take advantage of the erotic fantasies already swirling through %s's head, whispering ideas that fan the flame of %s lust.",
					new Object[] { target.name(), target.possessivePronoun() });
		}
		return String.format(
				"You plant an erotic suggestion in %s's mind, distracting %s with lewd fantasies.",
				new Object[] { target.name(), target.directObject() });
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.strong) {
			return String.format(
					"%s whispers a lewd suggestion to you, intensifying the fantasies you were trying to ignore and enflaming your arousal.",
					new Object[] { getSelf().name() });
		}
		return String.format(
				"%s gives you a hypnotic suggestion and your head is immediately filled with erotic possibilities.",
				new Object[] { getSelf().name() });
	}

}
