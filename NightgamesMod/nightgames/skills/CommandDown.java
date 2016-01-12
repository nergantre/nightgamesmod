package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Mount;
import nightgames.stance.Stance;

public class CommandDown extends PlayerCommand {

	@Override
	public boolean usable(Combat c, Character target) {
		return super.usable(c, target) && c.getStance().en == Stance.neutral;
	}

	public CommandDown(Character self) {
		super("Force Down", self);
	}

	@Override
	public String describe(Combat c) {
		return "Command your opponent to lie down on the ground.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		c.setStance(new Mount(getSelf(), target));
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new CommandDown(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int magnitude, Result modifier, Character target) {
		return "Trembling under the weight of your command, " + target.name()
				+ " lies down. You follow her down and mount her, facing her head.";
	}

	@Override
	public String receive(Combat c, int magnitude, Result modifier, Character target) {
		return "<<This should not be displayed, please inform The" + " Silver Bard: CommandDown-receive>>";
	}

}
