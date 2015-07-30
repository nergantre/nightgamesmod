package skills;

import characters.Character;
import combat.Combat;
import combat.Result;

public class CommandDismiss extends PlayerCommand {

	public CommandDismiss(Character self) {
		super("Force Dismiss", self);
	}

	public boolean usable(Combat c, Character target) {
		return super.usable(c, target) && target.pet != null;
	}

	@Override
	public String describe() {
		return "Have your thrall dismiss their pet.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		c.write(getSelf(),deal(c, 0, Result.normal, target));
		target.pet.remove();
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new CommandDismiss(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.summoning;
	}

	@Override
	public String deal(Combat c, int magnitude, Result modifier, Character target) {
		return "You think you briefly see a pang of regret in " + target.name()
				+ "'s eyes, but she quickly dismisses her "
				+ target.pet.toString().toLowerCase() + ".";
	}

	@Override
	public String receive(Combat c, int magnitude, Result modifier, Character target) {
		return "<<This should not be displayed, please inform The"
				+ " Silver Bard: CommandDismiss-receive>>";
	}

}
