package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Mount;

public class Tackle extends Skill {

	public Tackle(Character self) {
		super("Tackle", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().mobile(getSelf())&&c.getStance().mobile(target)&&!c.getStance().prone(getSelf())&&getSelf().canAct();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy(c))&&getSelf().check(Attribute.Power,target.knockdownDC()-getSelf().get(Attribute.Animism))){
			if(getSelf().get(Attribute.Animism)>=1){
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.special, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.special, target));
				}
				target.pain(c, 4+Global.random(6));
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.normal, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.normal, target));
				}
				target.pain(c, 3+Global.random(4));
			}
			c.setStance(new Mount(getSelf(),target));
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
	public int getMojoCost(Combat c) {
		return 15;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return (user.get(Attribute.Power)>=26&& !user.has(Trait.petite))|| user.get(Attribute.Animism)>=1;
	}

	@Override
	public Skill copy(Character user) {
		return new Tackle(user);
	}
	public int speed(){
		if(getSelf().get(Attribute.Animism)>=1){
			return 3;
		}
		else{
			return 1;
		}
	}
	public int accuracy(Combat c){
		int base = 80;
		if(getSelf().get(Attribute.Animism)>=1){
			base = 120;
		}
		return Math.round(Math.max(Math.min(150, 2.5f * (getSelf().get(Attribute.Power)
				- c.getOther(getSelf()).get(Attribute.Power)) + base), 40));
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	public String getLabel(Combat c){
		if(getSelf().get(Attribute.Animism)>=1){
			return "Pounce";
		}
		else{
			return getName(c);
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
			return getSelf().name()+" wiggles her butt cutely before leaping at you and pinning you to the floor.";
		}
		if(modifier==Result.miss){
			return getSelf().name()+" tries to tackle you, but you sidestep out of the way.";
		}
		else{
			return getSelf().name()+" bowls you over and sits triumphantly on your chest.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Knock opponent to ground and get on top of her";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
