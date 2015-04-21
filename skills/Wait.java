package skills;

import global.Global;
import status.Unreadable;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Wait extends Skill {

	public Wait(Character self) {
		super("Wait", self);
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return true;
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(bluff()){
			int m = Global.random(25);
			if(self.human()){
				c.write(self,deal(c,m,Result.special, target));
			}
			else{
				c.write(self,receive(c,m,Result.special, target));
			}
			self.heal(c, m);
			self.calm(25-m);
			self.spendMojo(20);
			self.add(new Unreadable(self));
		}
		else if(focused()&&!c.getStance().sub(self)){
			if(self.human()){
				c.write(self,deal(c,0,Result.strong, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.strong, target));
			}
			self.heal(c, Global.random(4));
			self.calm(Global.random(8));
			self.buildMojo(20);
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
			self.buildMojo(10);
			self.heal(c, Global.random(4));
		}
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Wait(user);
	}
	public int speed(){
		return 0;
	}

	@Override
	public Tactics type(Combat c) {
		if(bluff()||focused()){
			return Tactics.calming;
		}
		else{
			return Tactics.misc;
		}
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.special){
			return "You force yourself to look less tired and horny than you actually are. You even start to believe it yourself.";
		}
		else if(modifier==Result.strong){
			return "You take a moment to clear your thoughts, focusing your mind and calming your body.";
		}
		else{
			return "You bide your time, waiting to see what "+target.name()+" will do.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.special){
			return "Despite your best efforts, "+self.name()+" is still looking as calm and composed as ever. Either you aren't getting to her at all, or she's good at hiding it.";
		}
		else if(modifier==Result.strong){
			return self.name()+" closes her eyes and takes a deep breath. When she opens her eyes, she seems more composed.";
		}
		else{
			return self.name()+" hesitates, watching you closely.";
		}
	}

	public String toString(){
		if(bluff()){
			return "Bluff";
		}
		else if(focused()){
			return "Focus";
		}
		else{
			return name;
		}
	}

	@Override
	public String describe() {
		if(bluff()){
			return "Regain some stamina and lower arousal. Hides current status from opponent.";
		}
		else if(focused()){
			return "Calm yourself and gain some mojo";
		}
		else{
			return "Do nothing";
		}
	}

	private boolean focused(){
		return self.getPure(Attribute.Cunning)>=15 && !self.has(Trait.undisciplined)&&self.canRespond();
	}

	private boolean bluff(){
		return self.has(Trait.pokerface)&&self.getPure(Attribute.Cunning)>=9&&self.canSpend(20)&&self.canRespond();
	}
}
