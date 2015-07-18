package skills;

import status.Bound;
import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class Binding extends Skill {

	public Binding(Character self) {
		super("Binding", self, 4);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Arcane)>=9;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && !c.getStance().sub(getSelf())&&!c.getStance().prone(getSelf())&&!c.getStance().prone(target)&&getSelf().canAct()&&getSelf().canSpend(20);
	}

	@Override
	public String describe() {
		return "Bind your opponent's hands with a magic seal: 20 Mojo";
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
		target.add(new Bound(target,Math.min(10+3*getSelf().get(Attribute.Arcane), 50),"seal"));
		target.emote(Emotion.nervous, 5);
		getSelf().emote(Emotion.confident, 20);
		getSelf().emote(Emotion.dominant, 10);
	}

	@Override
	public Skill copy(Character user) {
		return new Binding(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You cast a binding spell on "+target.name()+", holding her in place.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" gestures at you and casts a spell. A ribbon of light wraps around your wrists and holds them in place.";
	}

}
