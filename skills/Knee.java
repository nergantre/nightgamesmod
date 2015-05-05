package skills;

import stance.StandingOver;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Knee extends Skill {

	public Knee(Character self) {
		super("Knee", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().mobile(self)&&!c.getStance().prone(self)&&self.canAct()&&!c.getStance().behind(target)&&!c.getStance().penetration(target);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy())){
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				if(c.getStance().prone(target)){
					c.write(self,receive(c,0,Result.special, target));
				}
				else{
					c.write(self,receive(c,0,Result.normal, target));
				}
				if(Global.random(5)>=3){
					c.write(self,self.bbLiner());
				}
			}
			if(target.has(Trait.achilles)&&!target.has(Trait.armored)){
				target.pain(c, 4+Global.random(6));
			}
			if(target.has(Trait.armored)){
				target.pain(c, Global.random(6)+self.get(Attribute.Power)/2);
			}
			else{
				target.pain(c, 4+Global.random(11)+self.get(Attribute.Power));
			}
			if(self.has(Trait.wrassler)){
				target.calm(c, Global.random(6));
			}
			else{
				target.calm(c, Global.random(10));
			}
			self.buildMojo(c, 10);
			target.emote(Emotion.angry,20);
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
		return user.get(Attribute.Power)>=10;
	}

	@Override
	public Skill copy(Character user) {
		return new Knee(user);
	}
	public int speed(){
		return 4;
	}
	public Tactics type(Combat c) {
		return Tactics.damage;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return target.name()+" blocks your knee strike.";
		}
		return "You deliver a powerful knee strike to "+target.name()+"'s delicate lady flower. She lets out a pained whimper and nurses her injured parts.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return self.name()+" tries to knee you in the balls, but you block it.";
		}
		if(modifier==Result.special){
			return self.name()+" raises one leg into the air, then brings her knee down like a hammer onto your balls. You cry out in pain and instinctively try " +
					"to close your legs, but she holds them open.";
		}
		else{
			return self.name()+" steps in close and brings her knee up between your legs, crushing your fragile balls. You groan and nearly collapse from the " +
					"intense pain in your abdomen.";
		}
	}

	@Override
	public String describe() {
		return "Knee opponent in the groin";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
