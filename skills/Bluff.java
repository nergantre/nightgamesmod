package skills;

import status.Unreadable;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Bluff extends Skill {

	public Bluff(Character self) {
		super("Bluff", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.has(Trait.pokerface)&&user.get(Attribute.Cunning)>=9;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().mobile(self)&&self.canSpend(20);
	}

	@Override
	public void resolve(Combat c, Character target) {
		int m = Global.random(25);
		self.spendMojo(c, 20);
		if(self.human()){
			c.write(self,deal(c,m,Result.normal, target));
		}
		else{
			c.write(self,receive(c,m,Result.normal, target));
		}
		self.heal(c, m);
		self.calm(c, 25-m);
		self.add(new Unreadable(self));
		self.emote(Emotion.confident, 30);
		self.emote(Emotion.dominant, 20);
		self.emote(Emotion.nervous,-20);
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
		return "Despite your best efforts, "+self.name()+" is still looking as calm and composed as ever. Either you aren't getting to her at all, or she's good at hiding it.";
	}

	@Override
	public String describe() {
		return "Regain some stamina and lower arousal. Hides current status from opponent.";
	}

}
