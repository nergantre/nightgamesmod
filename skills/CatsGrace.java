package skills;

import status.Nimble;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class CatsGrace extends Skill {

	public CatsGrace(Character self) {
		super("Cat's Grace", self);
	}
	
	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Animism)>=3;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&getSelf().getArousal().percent()>=20;
	}

	@Override
	public String describe() {
		return "Use your instinct to nimbly avoid attacks";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		getSelf().add(new Nimble(getSelf(),4));
	}

	@Override
	public Skill copy(Character user) {
		return new CatsGrace(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You rely on your animal instincts to quicken your movements and avoid attacks.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" focuses for a moment and her movements start to speed up and become more animalistic.";
	}

}
