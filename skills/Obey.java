package skills;

import status.Stsflag;
import characters.Character;
import combat.Combat;
import combat.Result;

public class Obey extends Skill {

	public Obey(Character self) {
		super("Obey", self);
	}
	
	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.is(Stsflag.enthralled) && !self.is(Stsflag.stunned);
	}

	@Override
	public String describe() {
		return "Obey the succubus' every command";
	}

	@Override
	public void resolve(Combat c, Character target) {
        if (self.human())
            c.write(self,"You patiently await your mistress' command");
        else if (target.human())
            c.write(self,self.name() + " stares ahead blankly, waiting for her orders.");
    }

	@Override
	public Skill copy(Character paramCharacter) {
		return new Obey(paramCharacter);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int paramInt,
			Result paramResult, Character paramCharacter) {
		return "";
	}

	@Override
	public String receive(Combat c, int paramInt,
			Result paramResult, Character paramCharacter) {
		return "";
	}

}
