package skills;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class LegLock extends Skill {

	public LegLock(Character self) {
		super("Leg Lock", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().dom(self)&&c.getStance().reachBottom(self)&&c.getStance().prone(target)&&self.canAct()&&!c.getStance().penetration(self);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy())){
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
			target.pain(c, Global.random(10)+7);
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

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Power)>=24;
	}

	@Override
	public Skill copy(Character user) {
		return new LegLock(user);
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
			return "You grab "+target.name()+"'s leg, but she kicks free.";
		}
		else{
			return "You take hold of "+target.name()+"'s ankle and force her leg to extend painfully.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return self.name()+" tries to put you in a leglock, but you slip away.";
		}
		else{
			return self.name()+" pulls your leg across her in a painful submission hold.";
		}
	}

	@Override
	public String describe() {
		return "A submission hold on your opponent's leg";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
