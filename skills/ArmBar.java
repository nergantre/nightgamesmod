package skills;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class ArmBar extends Skill {

	public ArmBar(Character self) {
		super("Armbar", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().dom(self)&&c.getStance().reachTop(self)&&self.canAct()&&!self.has(Trait.undisciplined)&&!c.getStance().penetration(self);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy()+self.tohit())){
			int m = Global.random(10)+self.get(Attribute.Power)/2;
			if(self.human()){
				c.write(self,deal(c,m,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,m,Result.normal, target));
			}
			target.pain(c, m);
			target.emote(Emotion.angry,15);
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.miss, target));
			}
		}
	}

	public boolean requirements() {
		return self.getPure(Attribute.Power)>=20 && !self.has(Trait.undisciplined);
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Power)>=20 && !user.has(Trait.undisciplined);
	}

	@Override
	public Skill copy(Character user) {
		return new ArmBar(user);
	}
	
	public int speed(){
		return 2;
	}
	public Tactics type(Combat c) {
		return Tactics.damage;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You grab at "+target.name()+"'s arm, but she pulls it free.";
		}
		else{
			return "You grab "+target.name()+"'s arm at the wrist and pull it to your chest in the traditional judo submission technique.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return self.name()+" grabs your wrist, but you pry it out of her grasp.";
		}
		else{
			return self.name()+" pulls your arm between her legs, forceably overextending your elbow. The pain almost makes you tap out, but you manage to yank your arm " +
				"out of her grip.";
		}
	}

	@Override
	public String describe() {
		return "A judo submission hold that hyperextends the arm.";
	}
}
