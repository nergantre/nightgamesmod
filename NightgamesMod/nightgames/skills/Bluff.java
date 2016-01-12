package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Unreadable;

public class Bluff extends Skill {

	public Bluff(Character self) {
		super("Bluff", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.has(Trait.pokerface)&&user.get(Attribute.Cunning)>=9;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf());
	}
	
	@Override
	public int getMojoCost(Combat c) {
		return 20;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int m = Global.random(25);
		if(getSelf().human()){
			c.write(getSelf(),deal(c,m,Result.normal, target));
		}
		else{
			c.write(getSelf(),receive(c,m,Result.normal, target));
		}
		getSelf().heal(c, m);
		getSelf().calm(c, 25-m);
		getSelf().add(c, new Unreadable(getSelf()));
		getSelf().emote(Emotion.confident, 30);
		getSelf().emote(Emotion.dominant, 20);
		getSelf().emote(Emotion.nervous,-20);
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Bluff(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.calming;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You force yourself to look less tired and horny than you actually are. You even start to believe it yourself.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return "Despite your best efforts, "+getSelf().name()+" is still looking as calm and composed as ever. Either you aren't getting to her at all, or she's good at hiding it.";
	}

	@Override
	public String describe(Combat c) {
		return "Regain some stamina and lower arousal. Hides current status from opponent.";
	}

}
