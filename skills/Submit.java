package skills;

import stance.Stance;
import stance.StandingOver;
import characters.Character;
import characters.Trait;
import combat.Combat;
import combat.Result;

public class Submit extends Skill {

	public Submit(Character self) {
		super("Submit", self);
		
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (c.getStance().en == Stance.neutral) &&getSelf().canAct();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		c.setStance(new StandingOver(target, getSelf()));
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return user.has(Trait.submissive) || user.human();
	}

	@Override
	public Skill copy(Character user) {
		return new Submit(user);
	}
	public int speed(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You nervously lie down on the floor.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" with a nervous glance, lies down on the floor.";
	}

	@Override
	public String describe() {
		return "Submits to your opponent by lying down.";
	}
}
