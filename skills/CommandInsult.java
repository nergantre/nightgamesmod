package skills;

import characters.Character;
import characters.Trait;
import combat.Combat;
import combat.Result;
import stance.Stance;

public class CommandInsult extends PlayerCommand {

	public CommandInsult(Character self) {
		super("Insult", self);
	}

	@Override
	public String describe() {
		return "Temporarily destroy your thrall's self-image, draining their mojo.";
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 10;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		target.loseMojo(c, 15);
		c.write(getSelf(),deal(c, 0, Result.normal, target));
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new CommandInsult(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int magnitude, Result modifier, Character target) {
		return "Your words nearly drive " + target.name()
				+ " to tears with their ferocity and psychic backup. Luckily,"
				+ " she won't remember any of it later.";
	}

	@Override
	public String receive(Combat c, int magnitude, Result modifier, Character target) {
		return "<<This should not be displayed, please inform The"
				+ " Silver Bard: CommandInsult-receive>>";
	}

}
