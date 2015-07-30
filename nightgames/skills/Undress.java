package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Neutral;
import nightgames.stance.Stance;

public class Undress extends Skill {

	public Undress(Character self) {
		super("Undress", self);
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&!c.getStance().sub(getSelf())&&!getSelf().nude()&&!c.getStance().prone(getSelf())&&!getSelf().has(Trait.strapped);
	}

	@Override
	public String describe() {
		return "Removes your own clothes";
	}

	@Override
	public boolean resolve(Combat c, Character target) {	
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		getSelf().undress(c);
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Undress(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (c.getStance().en != Stance.neutral) {
			return "You wiggle out of your clothes and toss them aside";
		}
		return "You quickly strip off your clothes and toss them aside.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if (c.getStance().en != Stance.neutral) {
			return getSelf().name() + " wiggles out of her clothes and toss them aside";
		}
		return getSelf().name()+" puts some space between you and strips naked.";
	}
}
