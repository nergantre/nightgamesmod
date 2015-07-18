package skills;

import stance.StandingOver;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class HipThrow extends Skill {

	public HipThrow(Character self) {
		super("HipThrow", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.has(Trait.judonovice);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().mobile(getSelf())&&c.getStance().mobile(target)&&!c.getStance().prone(getSelf())&&!c.getStance().prone(target)&&getSelf().canAct()&&!c.getStance().penetration(getSelf())&&getSelf().canSpend(10);
	}

	@Override
	public void resolve(Combat c, Character target) {
		getSelf().spendMojo(c, 10);
		if(getSelf().check(Attribute.Power,target.knockdownDC())){
			int m = Global.random(6)+target.get(Attribute.Power)/2;
			if(getSelf().human()){
				c.write(getSelf(),deal(c,m,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,m,Result.normal, target));
			}
			target.pain(c, m);
			c.setStance(new StandingOver(getSelf(),target));
			target.emote(Emotion.angry,5);
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
		}
	}

	@Override
	public Skill copy(Character user) {
		return new HipThrow(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.damage;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.normal){
			return target.name()+" rushes toward you, but you step in close and pull her towards you, using her momentum to throw her across your hip and onto the floor.";
		}
		else{
			return "As "+target.name()+" advances, you pull her towards you and attempt to throw her over your hip, but she steps away from the throw and manages to keep her footing.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.normal){
			return "You see a momentary weakness in "+getSelf().name()+"'s guard and lunge toward her to take advantage of it. The next thing you know, you're hitting the floor behind her.";
		}
		else{
			return getSelf().name()+" grabs your arm and pulls you off balance, but you manage to plant your foot behind her leg sweep. This gives you a more stable stance than her and she has " +
					"to break away to stay on her feet.";
		}
	}

	@Override
	public String describe() {
		return "Throw your opponent to the ground, dealing some damage: 10 Mojo";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
