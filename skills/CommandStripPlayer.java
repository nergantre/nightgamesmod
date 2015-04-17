package skills;

import characters.Character;
import combat.Combat;
import combat.Result;

public class CommandStripPlayer extends PlayerCommand {

	public CommandStripPlayer(Character self) {
		super("Force Strip Player", self);
	}

	public boolean usable(Combat c, Character target) {
		return super.usable(c, target) && !self.nude();
	}
	
	@Override
	public String describe() {
		return "Make your thrall undress you.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.undress(c);
		if (self.human())
			c.write(self,deal(c, 0, Result.normal, target));
	}

	@Override
	public Skill copy(Character user) {
		return new CommandStripPlayer(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int magnitude, Result modifier, Character target) {
		return "With an elated gleam in her eyes, " + target.name() + 
				" moves her hands with nigh-inhuman dexterity, stripping all"
				+ " of your clothes in just a second.";
	}

	@Override
	public String receive(Combat c, int magnitude, Result modifier, Character target) {
		return "<<This should not be displayed, please inform The"
				+ " Silver Bard: CommandStripPlayer-receive>>";
	}

}
