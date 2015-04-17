package skills;

import characters.Character;
import combat.Combat;
import combat.Result;

public class CommandInsult extends PlayerCommand {

	public CommandInsult(Character self) {
		super("Insult", self);
	}

	@Override
	public String describe() {
		return "Temporarily destroy your thrall's self-image, draining their mojo.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if (target.canSpend(15))
			target.spendMojo(15);
		else
			target.getMojo().set(0);
		self.buildMojo(10);
		c.write(self,deal(c, 0, Result.normal, target));
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
