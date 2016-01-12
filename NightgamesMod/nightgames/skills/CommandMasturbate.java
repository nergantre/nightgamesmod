package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class CommandMasturbate extends PlayerCommand {

	public CommandMasturbate(Character self) {
		super("Force Masturbation", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return super.usable(c, target) && target.crotchAvailable();
	}

	@Override
	public String describe(Combat c) {
		return "Convince your opponents to pleasure themselves for your viewing pleasure";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		boolean lowStart = target.getArousal().get() < 15;
		int m = 5 + Global.random(10);
		target.body.pleasure(target, target.body.getRandom("hands"), target.body.getRandom("cock"), m, c);

		boolean lowEnd = target.getArousal().get() < 15;
		if (getSelf().human()) {
			if (lowStart) {
				if (lowEnd) {
					c.write(getSelf(), deal(c, 0, Result.weak, target));
				} else {
					c.write(getSelf(), deal(c, 0, Result.strong, target));
				}
			} else {
				c.write(getSelf(), deal(c, 0, Result.normal, target));
			}
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new CommandMasturbate(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int magnitude, Result modifier, Character target) {
		switch (modifier) {
		case normal:
			return target.name() + " seems more than happy to do as you tell her, "
					+ "as she starts fingering herself in abandon.";
		case special:
			return "Looking at you lustily, " + target.name() + " rubs her clit as she gets wetter and wetter.";
		case weak:
			return target.name() + " follows your command to the letter, but"
					+ " it doesn't seem to have that much of an effect on her.";
		default:
			return "<<This should not be displayed, please inform The" + " Silver Bard: CommandMasturbate-deal>>";
		}
	}

	@Override
	public String receive(Combat c, int magnitude, Result modifier, Character target) {
		return "<<This should not be displayed, please inform The" + " Silver Bard: CommandMasturbate-receive>>";
	}

}
