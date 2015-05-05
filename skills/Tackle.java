package skills;

import stance.Mount;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Tackle extends Skill {

	public Tackle(Character self) {
		super("Tackle", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().mobile(self)&&c.getStance().mobile(target)&&!c.getStance().prone(self)&&self.canAct()&&!self.has(Trait.petite);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy())&&self.check(Attribute.Power,target.knockdownDC()-self.get(Attribute.Animism))){
			if(self.get(Attribute.Animism)>=1){
				if(self.human()){
					c.write(self,deal(c,0,Result.special, target));
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.special, target));
				}
				target.pain(c, 4+Global.random(6));
			}
			else{
				if(self.human()){
					c.write(self,deal(c,0,Result.normal, target));
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.normal, target));
				}
				target.pain(c, 3+Global.random(4));
			}
			c.setStance(new Mount(self,target));
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
		return (user.get(Attribute.Power)>=26&& !user.has(Trait.petite))|| user.get(Attribute.Animism)>=1;
	}

	@Override
	public Skill copy(Character user) {
		return new Tackle(user);
	}
	public int speed(){
		if(self.get(Attribute.Animism)>=1){
			return 3;
		}
		else{
			return 1;
		}
	}
	public int accuracy(){
		if(self.get(Attribute.Animism)>=1){
			return 3;
		}
		else{
			return 1;
		}
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	public String getLabel(Combat c){
		if(self.get(Attribute.Animism)>=1){
			return "Pounce";
		}
		else{
			return getName();
		}
	}
	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.special){
			return "You let your instincts take over and you pounce on "+target.name()+" like a predator catching your prey.";
		}
		else if(modifier==Result.normal){
			return "You tackle "+target.name()+" to the ground and straddle her.";
		}
		else{
			return "You lunge at "+target.name()+", but she dodges out of the way.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.special){
			return self.name()+" wiggles her butt cutely before leaping at you and pinning you to the floor.";
		}
		if(modifier==Result.miss){
			return self.name()+" tries to tackle you, but you sidestep out of the way.";
		}
		else{
			return self.name()+" bowls you over and sits triumphantly on your chest.";
		}
	}

	@Override
	public String describe() {
		return "Knock opponent to ground and get on top of her";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
