package skills;

import status.Alluring;
import status.Distorted;
import status.Nimble;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Illusions extends Skill {

	public Illusions(Character self) {
		super("Illusions", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Arcane)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf())&&getSelf().canSpend(20);
	}

	@Override
	public String describe() {
		return "Create illusions to act as cover: 20 Mojo";
	}

	@Override
	public void resolve(Combat c, Character target) {
		getSelf().spendMojo(c, 20);
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		getSelf().add(new Distorted(getSelf(), 6));
		getSelf().add(new Alluring(getSelf(), 5));
	}

	@Override
	public Skill copy(Character user) {
		return new Illusions(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You cast an illusion spell to create seveal images of yourself. At the same time, you add a charm to make yourself irresistable.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" casts a brief spell and your vision is filled with naked copies of her. You can still tell which "+getSelf().name()+" is real, but it's still a distraction. At the same time, she suddnely looks irresistable.";
	}
}
