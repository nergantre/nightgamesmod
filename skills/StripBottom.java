package skills;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class StripBottom extends Skill {

	public StripBottom(Character self) {
		super("Strip Bottoms", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().reachBottom(self)&&!target.pantsless()&&self.canAct();
	}

	@Override
	public void resolve(Combat c, Character target) {
		int difficulty = target.bottom.peek().dc()
				+(target.getLevel())
				+(target.getStamina().percent() / 2
				- target.getArousal().percent()) / 4
				- (c.getStance().dom(self) ? 50 : 0);
		if (self.check(Attribute.Cunning, difficulty)||!target.canAct()) {
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
			target.strip(1, c);
			if(self.human()&&target.nude()){
				c.write(target,target.nakedLiner());
			}
			if(target.human()&&target.pantsless()){
				if(target.getArousal().get()>=15){
					c.write("Your boner springs out, no longer restrained by your pants.");
				}
				else{
					c.write(self.name()+" giggles as your flacid dick is exposed");
				}
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
		return new StripBottom(user);
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
			return "You grab "+target.name()+"'s "+target.bottom.peek().getName()+", but she scrambles away before you can strip her.";
		}
		else{
			return "After a brief struggle, you manage to pull off "+target.name()+"'s "+target.bottom.peek().getName()+".";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return self.name()+" tries to pull down your "+target.bottom.peek().getName()+", but you hold them up.";
		}
		else{
			return self.name()+" grabs the waistband of your "+target.bottom.peek().getName()+" and pulls them down.";
		}
	}

	@Override
	public String describe() {
		return "Attempt to remove opponent's pants. More likely to succeed if she's weakened and aroused";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
