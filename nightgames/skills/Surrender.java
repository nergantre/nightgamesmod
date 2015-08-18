package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Mount;
import nightgames.stance.StandingOver;

public class Surrender extends Skill {

	public Surrender(Character self) {
		super("Surrender", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.human();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		getSelf().tempt(c, getSelf().getArousal().max());
		getSelf().loseWillpower(c, getSelf().getWillpower().max());
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Surrender(user);
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
				getSelf().subject(), target.possessivePronoun(), Global.capitalizeFirstLetter(getSelf().pronoun()), getSelf().reflectivePronoun());
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return String.format("After giving up on the fight, %s start fantasizing about %s body. %s quickly find %s at the edge.",
				getSelf().subject(), target.possessivePronoun(), Global.capitalizeFirstLetter(getSelf().pronoun()), getSelf().reflectivePronoun());
	}

	@Override
	public String describe() {
		return "Give up";
	}
}
