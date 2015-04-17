package skills;

import global.Global;
import stance.Mount;
import stance.StandingOver;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Edging extends Skill {

	public Edging(Character self) {
		super("Edging", self);
	}

	@Override
	public boolean requirements() {
		return self.human();
	}

	@Override
	public boolean requirements(Character user) {
		return user.human();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.tempt(c, self.getArousal().max());
	}

	@Override
	public Skill copy(Character user) {
		return new Edging(user);
	}
	public int speed(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return String.format("After giving up on the fight, %s start fantasizing about %s body. %s quickly find %s at the edge.",
				self.subject(), target.possessivePronoun(), Global.capitalizeFirstLetter(self.pronoun()), self.reflectivePronoun());
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return String.format("After giving up on the fight, %s start fantasizing about %s body. %s quickly find %s at the edge.",
				self.subject(), target.possessivePronoun(), Global.capitalizeFirstLetter(self.pronoun()), self.reflectivePronoun());
	}

	@Override
	public String describe() {
		return "Give up";
	}
}
