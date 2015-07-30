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
	public boolean requirements(Character user) {
		return user.get(Attribute.Fetish)>=6;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canRespond()&&c.getStance().mobile(getSelf())&&getSelf().getArousal().get()>=5&&!getSelf().is(Stsflag.bondage);
	}

	@Override
	public String describe() {
		return "You and your opponent become aroused by being tied up for five turns: Arousal at least 5";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		getSelf().add(c, new BD(getSelf()));
		target.add(c, new BD(target));
		return true;
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
		return getSelf().name()+" flushes and wraps her arms around herself tightly. Suddenly the thought of being tied up and dominated slips into your head.";
	}

}
