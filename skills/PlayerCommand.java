package skills;

import status.Enthralled;
import status.Stsflag;
import combat.Combat;
import characters.Character;

public abstract class PlayerCommand extends Skill {

	public PlayerCommand(String name, Character self) {
		super(name, self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.human();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.human()&& target.is(Stsflag.enthralled)
				&& ((Enthralled) target.getStatus(Stsflag.enthralled)).master
						.equals(self) && !c.getStance().penetration(self) && self.canRespond();
	}
}
