package skills;

import combat.Combat;
import combat.Result;
import characters.Character;

public class CommandStrip extends PlayerCommand {

	public CommandStrip(Character self) {
		super("Force Strip Self", self);
	}

	public boolean usable(Combat c, Character target) {
		return super.usable(c, target) && !target.nude();
	}
	
	@Override
	public String describe() {
		return "Force your opponent to strip naked.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		target.undress(c);
		if (target.human())
			c.write(getSelf(),receive(c, 0, Result.normal, target));
		else
			c.write(getSelf(),deal(c, 0, Result.normal, target));
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new CommandStrip(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int magnitude, Result modifier, Character target) {
		return "You look "
				+ target.name()
				+ " in the eye, sending a psychic command for"
				+ " her to strip. She complies without question, standing before you nude only"
				+ " seconds later.";
	}

	@Override
	public String receive(Combat c, int magnitude, Result modifier, Character target) {
		return "<<This should not be displayed, please inform The Silver Bard: CommandStrip-receive>>";
	}

}
