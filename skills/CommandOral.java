package skills;

import global.Global;
import characters.Character;
import characters.Trait;
import combat.Combat;
import combat.Result;

public class CommandOral extends PlayerCommand {

	public CommandOral(Character self) {
		super("Force Oral", self);
	}

	public boolean usable(Combat c, Character target) {
		return super.usable(c, target) && self.pantsless()
				&& c.getStance().reachBottom(self);
	}
	
	@Override
	public String describe() {
		return "Force your opponent to go down on you.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		boolean silvertongue = target.has(Trait.silvertongue);
		boolean lowStart = self.getArousal().get() < 15;
		int m = (silvertongue ? 8 : 5) + Global.random(10);
		if (self.human())
			if (lowStart)
				if (m < 8)
					c.write(self,deal(c, 0, Result.weak, target));
				else
					c.write(self,deal(c, 0, Result.strong, target));
			else
				c.write(self,deal(c, 0, Result.normal, target));
		self.body.pleasure(target, target.body.getRandom("mouth"), self.body.getRandom("cock"), m, c);					
		self.buildMojo(30);
	}

	@Override
	public Skill copy(Character user) {
		return new CommandOral(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int magnitude, Result modifier, Character target) {
		switch (modifier) {
		case normal:
			return target.name() + " is ecstatic at being given the privilege of"
					+ " pleasuring you and does a fairly good job at it, too. She"
					+ " sucks your hard dick powerfully while massaging your balls.";
		case strong:
			return target.name() + " seems delighted to 'help' you, and makes short work"
					+ " of taking your flaccid length into her mouth and getting it "
					+ "nice and hard.";
		case weak:
			return target.name() + " tries her very best to get you ready by running"
					+ " her tounge all over your groin, but even"
					+ " her psychically induced enthusiasm can't get you hard.";
		default:
			return "<<This should not be displayed, please inform The"
			+ " Silver Bard: CommandOral-deal>>";
		}
	}

	@Override
	public String receive(Combat c, int magnitude, Result modifier, Character target) {
		return "<<This should not be displayed, please inform The"
				+ " Silver Bard: CommandOral-receive>>";
	}

}
