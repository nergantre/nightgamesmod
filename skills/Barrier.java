package skills;

import status.Shield;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Barrier extends Skill {

	public Barrier(Character self) {
		super("Barrier", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Arcane)>=18;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !c.getStance().sub(getSelf())&&!c.getStance().prone(getSelf())&&!c.getStance().prone(target)&&getSelf().canAct()&&getSelf().canSpend(3);
	}

	@Override
	public String describe() {
		return "Creates a magical barrier to protect you from physical damage: 3 Mojo";
	}

	@Override
	public void resolve(Combat c, Character target) {
		getSelf().spendMojo(c, 3);
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		getSelf().add(new Shield(getSelf(), .5));
	}

	@Override
	public Skill copy(Character user) {
		return new Barrier(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.recovery;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You conjure a simple magic barrier around yourself, reducing physical damage. Unfortunately, it will do nothing against a gentle carress.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" holds a hand in front of her and you see a magical barrier appear briefly, before it becomes invisible.";
	}

}
