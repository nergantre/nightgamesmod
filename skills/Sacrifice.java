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
		return self.canAct()&&!c.getStance().sub(self)&&self.getArousal().percent()>=70&&self.canSpend(25);
	}

	@Override
	public String describe() {
		return "Damage yourself to reduce arousal: 25 Mojo";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.spendMojo(c, 25);
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.weaken(c, 20 + self.get(Attribute.Dark));
		self.calm(c, 20 + self.get(Attribute.Dark));
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
		return self.name()+" pinches her nipples hard while screaming in pain. You see her stagger in exhaustion, but she seems much less aroused.";
	}
}
