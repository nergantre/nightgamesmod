package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class LegLock extends Skill {

	public LegLock(Character self) {
		super("Leg Lock", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().dom(getSelf())&&c.getStance().reachBottom(getSelf())&&c.getStance().prone(target)&&getSelf().canAct()&&!c.getStance().connected();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy(c))){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			target.pain(c, Global.random(10)+7);
			target.emote(Emotion.angry,15);
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
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
			return getSelf().name()+" tries to put you in a leglock, but you slip away.";
		}
		else{
			return getSelf().name()+" pulls your leg across her in a painful submission hold.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "A submission hold on your opponent's leg";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
