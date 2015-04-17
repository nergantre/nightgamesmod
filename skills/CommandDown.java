package skills;

import stance.Mount;
import stance.Stance;
import characters.Character;
import combat.Combat;
import combat.Result;

public class CommandDown extends PlayerCommand {

	public boolean usable(Combat c, Character target) {
		return super.usable(c, target) && c.getStance().en == Stance.neutral;
	}

	public CommandDown(Character self) {
		super("Force Down", self);
	}

	@Override
	public String describe() {
		return "Command your opponent to lay down on the ground.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		c.setStance(new Mount(self, target));
		if (self.human())
			c.write(self,deal(c, 0, Result.normal, target));
		else
			c.write(self,receive(c, 0, Result.normal, target));
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
		return "<<This should not be displayed, please inform The"
				+ " Silver Bard: CommandDown-receive>>";
	}

}
