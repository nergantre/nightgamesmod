package skills;

import status.Stsflag;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Sacrifice extends Skill {

	public Sacrifice(Character self) {
		super("Sacrifice", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Dark)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&!c.getStance().sub(getSelf())&&getSelf().getArousal().percent()>=70&&getSelf().canSpend(25);
	}

	@Override
	public String describe() {
		return "Damage yourself to reduce arousal: 25 Mojo";
	}

	@Override
	public void resolve(Combat c, Character target) {
		getSelf().spendMojo(c, 25);
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		getSelf().weaken(c, 20 + getSelf().get(Attribute.Dark));
		getSelf().calm(c, 20 + getSelf().get(Attribute.Dark));
	}

	@Override
	public Skill copy(Character user) {
		return new Sacrifice(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.calming;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You feed your own lifeforce and pleasure to the darkness inside you. Your legs threaten to give out, but you've regained some self control.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" pinches her nipples hard while screaming in pain. You see her stagger in exhaustion, but she seems much less aroused.";
	}
}
