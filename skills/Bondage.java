package skills;

import status.BD;
import status.Stsflag;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Bondage extends Skill {

	public Bondage(Character self) {
		super("Bondage", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Fetish)>=6;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Fetish)>=6;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canRespond()&&c.getStance().mobile(self)&&self.getArousal().get()>=5&&!self.is(Stsflag.bondage);
	}

	@Override
	public String describe() {
		return "You and your opponent become aroused by being tied up for five turns: Arousal at least 5";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.add(new BD(self));
		target.add(new BD(target));
	}

	@Override
	public Skill copy(Character user) {
		return new Bondage(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You imagine the exhilerating feeling of ropes digging into your skin and binding you. You push this feeling into "+target.name()+"'s libido.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" flushes and wraps her arms around herself tightly. Suddenly the thought of being tied up and dominated slips into your head.";
	}

}
