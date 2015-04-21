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
		return c.getStance().reachTop(self)&&!target.topless()&&self.canAct();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.check(Attribute.Cunning, target.top.peek().dc()+(target.getLevel())+(target.getStamina().percent()-target.getArousal().percent())/4)||!target.canAct()){
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
			target.strip(0, c);
			if(self.human()&&target.nude()){
				c.write(target,target.nakedLiner());
			}
			target.emote(Emotion.nervous, 10);
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.miss, target));
			}
			target.weaken(c, Global.random(6)+self.get(Attribute.Power)/4);
		}
	}

	@Override
	public boolean requirements() {
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
			return self.name()+" tries to yank off your "+target.top.peek().getName()+", but you manage to hang onto it.";
		}else{
			return self.name()+" grabs a hold of your "+target.top.peek().getName()+" and yanks it off before you can stop her.";
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
