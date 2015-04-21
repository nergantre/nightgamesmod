package skills;

import status.Shamed;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Slap extends Skill {

	public Slap(Character self) {
		super("Slap", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().reachTop(self)&&self.canAct()&&!self.has(Trait.softheart)&&!c.getStance().behind(self);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy()+self.tohit())){
			if(self.getPure(Attribute.Animism)>=8){
				if(self.human()){
					c.write(self,deal(c,0,Result.special, target));			
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.special, target));
				}
				if(self.has(Trait.pimphand)){
					target.pain(c, Global.random(16*(self.getArousal().percent()/100))+self.get(Attribute.Power)/2);
					target.calm(Global.random(4)+2);
					target.emote(Emotion.nervous, 40);
					target.emote(Emotion.angry, 30);
					self.buildMojo(20);
				}
				else{
					target.pain(c, Global.random(12*(self.getArousal().percent()/100)+1)+self.get(Attribute.Power)/2);
					target.calm(Global.random(5)+4);
					target.emote(Emotion.nervous, 25);
					target.emote(Emotion.angry, 30);
					self.buildMojo(10);
				}
			}
			else{
				if(self.human()){
					c.write(self,deal(c,0,Result.normal, target));			
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.normal, target));
				}
				if(self.has(Trait.pimphand)){
					target.pain(c, Global.random(8)+5+target.get(Attribute.Perception));
					target.calm(Global.random(4)+2);
					target.emote(Emotion.nervous, 20);
					target.emote(Emotion.angry, 30);
					self.buildMojo(20);
				}
				else{
					target.pain(c, Global.random(5)+4);
					target.calm(Global.random(5)+4);
					target.emote(Emotion.nervous, 10);
					target.emote(Emotion.angry, 30);
					self.buildMojo(10);
				}
			}
			
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
	public boolean requirements() {
		return !self.has(Trait.softheart)&&self.getPure(Attribute.Power)>=7;
	}

	@Override
	public boolean requirements(Character user) {
		return !user.has(Trait.softheart)&&user.getPure(Attribute.Power)>=7;
	}

	@Override
	public Skill copy(Character user) {
		return new Slap(user);
	}
	public int speed(){
		return 8;
	}
	public Tactics type(Combat c) {
		return Tactics.damage;
	}
	public String toString(){
		if(self.getPure(Attribute.Animism)>=8){
			return "Tiger Claw";
		}
		else{
			return "Slap";
		}
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return target.name()+" avoids your slap.";
		}
		else if(modifier==Result.special){
			return "You channel your beastial power and strike"+target.name()+" with a solid open hand strike.";
		}
		else{
			return "You slap "+target.name()+"'s cheek; not hard enough to really hurt her, but enough to break her concentration.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return self.name()+" tries to slap you but you catch her wrist.";
		}
		else if(modifier==Result.special){
			return self.name()+"'s palm hits you in a savage strike that makes your head ring.";
		}
		else{
			return self.name()+" slaps you across the face, leaving a stinging heat on your cheek.";
		}
	}

	@Override
	public String describe() {
		return "Slap opponent across the face";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
