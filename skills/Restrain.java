package skills;

import stance.Pin;
import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class Restrain extends Skill {

	public Restrain(Character self) {
		super("Pin", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().mobile(self)&&c.getStance().prone(target)&&c.getStance().reachTop(self)&&self.canAct()&&c.getStance().reachTop(target)&&!c.getStance().penetration(self)&&!c.getStance().penetration(target);
	}

	@Override
	public void resolve(Combat c, Character target) {
		resolve(c, target, false);
	}

	public void resolve(Combat c, Character target, boolean nofail) {
		if(nofail || target.roll(this, c, accuracy())) {
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
			c.setStance(new Pin(self,target));
			target.emote(Emotion.nervous, 10);
			target.emote(Emotion.desperate, 10);
			self.emote(Emotion.dominant, 20);
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
		return user.get(Attribute.Power)>=8;
	}

	@Override
	public Skill copy(Character user) {
		return new Restrain(user);
	}
	public int speed(){
		return 2;
	}
	public int accuracy(){
		return 4;
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You try to catch "+target.name()+"'s hands, but she squirms to much to keep your grip on her.";
		}
		else{
			return "You manage to restrain "+target.name()+", leaving her helpless and vulnerable beneath you.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return self.name()+" tries to pin you down, but you keep your arms free.";
		}
		else{
			return self.name()+" pounces on you and pins your arms in place, leaving you at her mercy.";
		}
	}

	@Override
	public String describe() {
		return "Restrain opponent until she struggles free";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
