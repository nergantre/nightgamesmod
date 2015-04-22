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
	public boolean requirements() {
		return self.getPure(Attribute.Arcane)>=9;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Arcane)>=9;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && !c.getStance().sub(self)&&!c.getStance().prone(self)&&!c.getStance().prone(target)&&self.canAct()&&self.canSpend(20);
	}

	@Override
	public String describe() {
		return "Bind your opponent's hands with a magic seal: 20 Mojo";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.spendMojo(c, 20);
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		target.add(new Bound(target,Math.min(10+3*self.get(Attribute.Arcane), 50),"seal"));
		target.emote(Emotion.nervous, 5);
		self.emote(Emotion.confident, 20);
		self.emote(Emotion.dominant, 10);
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
		return self.name()+" gestures at you and casts a spell. A ribbon of light wraps around your wrists and holds them in place.";
	}

}
