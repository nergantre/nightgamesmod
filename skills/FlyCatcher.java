package skills;

import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class FlyCatcher extends Skill {

	public FlyCatcher(Character self) {
		super("Fly Catcher", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Ki)>=9;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Ki)>=9;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.pet!=null&&self.canAct()&&c.getStance().mobile(self)&&!c.getStance().prone(self)&&self.canSpend(15);
	}

	@Override
	public String describe() {
		return "Focus on eliminating the enemy pet: 5 Stamina";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.pet!=null){
			target.pet.caught(c, self);
		}
		self.weaken(c, 5);
	}

	@Override
	public Skill copy(Character user) {
		return new FlyCatcher(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.summoning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}

}
