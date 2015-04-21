package skills;

import stance.Behind;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Maneuver extends Skill {

	public Maneuver(Character self) {
		super("Manuever", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Cunning)>=20;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().mobile(self)&&!c.getStance().prone(self)&&!c.getStance().prone(target)&&self.canSpend(8)&&!c.getStance().behind(self)&&self.canAct()&&!self.has(Trait.undisciplined)&&!c.getStance().penetration(self);
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.spendMojo(8);
		if(target.roll(this, c, accuracy()+self.tohit())){
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}	
			c.setStance(new Behind(self,target));
			self.emote(Emotion.confident, 15);
			self.emote(Emotion.dominant, 15);
			target.emote(Emotion.nervous,10);
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
		return user.getPure(Attribute.Cunning)>=20;
	}

	@Override
	public Skill copy(Character user) {
		return new Maneuver(user);
	}
	public int speed(){
		return 8;
	}
	public int accuracy(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You try to get behind "+target.name()+" but are unable to.";
		}
		else{
			return "You dodge past "+target.name()+"'s guard and grab her from behind.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return self.name()+" tries to slip behind you, but you're able to keep her in sight.";
		}
		else{
			return self.name()+" lunges at you, but when you try to grab her, she ducks out of sight. Suddenly her arms are wrapped around you. How did she get behind you?";
		}
	}

	@Override
	public String describe() {
		return "Get behind opponent: 8 Mojo";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
