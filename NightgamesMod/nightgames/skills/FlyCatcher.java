package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;

public class FlyCatcher extends Skill {

	public FlyCatcher(Character self) {
		super("Fly Catcher", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Ki)>=9;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.pet!=null&&getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf());
	}

	@Override
	public int getMojoCost(Combat c) {
		return 15;
	}

	@Override
	public String describe(Combat c) {
		return "Focus on eliminating the enemy pet: 5 Stamina";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.pet!=null){
			target.pet.caught(c, getSelf());
		}
		getSelf().weaken(c, 5);

		return true;
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
