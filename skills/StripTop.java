package skills;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class StripTop extends Skill {

	public StripTop(Character self) {
		super("Strip Top", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().reachTop(getSelf())&&!target.topless()&&getSelf().canAct();
	}

	@Override
	public int getMojoCost(Combat c) {
		return 10;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int difficulty = target.top.peek().dc()
				+(target.getLevel())
				+(target.getStamina().percent() / 5
				- target.getArousal().percent()) / 4
				- (c.getStance().dom(getSelf()) ? 50 : 0);
		if (getSelf().check(Attribute.Cunning, difficulty)||!target.canAct()) {
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			target.strip(0, c);
			if(getSelf().human()&&target.nude()){
				c.write(target,target.nakedLiner());
			}
			target.emote(Emotion.nervous, 10);
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
			target.weaken(c, Global.random(6)+getSelf().get(Attribute.Power)/4);
			return false;
		}
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new StripTop(user);
	}
	public int speed(){
		return 3;
	}
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You attempt to strip off "+target.name()+"'s "+target.top.peek().getName()+", but she shoves you away.";
		}else{
			return "After a brief struggle, you manage to pull off "+target.name()+"'s "+target.top.peek().getName()+".";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return getSelf().name()+" tries to yank off your "+target.top.peek().getName()+", but you manage to hang onto it.";
		}else{
			return getSelf().name()+" grabs a hold of your "+target.top.peek().getName()+" and yanks it off before you can stop her.";
		}
	}

	@Override
	public String describe() {
		return "Attempt to remove opponent's top. More likely to succeed if she's weakened and aroused";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
