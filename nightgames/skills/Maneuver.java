package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Behind;

public class Maneuver extends Skill {

	public Maneuver(Character self) {
		super("Manuever", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf())&&!c.getStance().prone(target)&&!c.getStance().behind(getSelf())&&getSelf().canAct()&&!getSelf().has(Trait.undisciplined)&&!c.getStance().penetration(getSelf());
	}
	@Override
	public int getMojoCost(Combat c) {
		return 8;
	}
	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy())){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}	
			c.setStance(new Behind(getSelf(),target));
			getSelf().emote(Emotion.confident, 15);
			getSelf().emote(Emotion.dominant, 15);
			target.emote(Emotion.nervous,10);
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
	public boolean requirements(Character user) {
		return user.get(Attribute.Cunning)>=20;
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
			return getSelf().name()+" tries to slip behind you, but you're able to keep her in sight.";
		}
		else{
			return getSelf().name()+" lunges at you, but when you try to grab her, she ducks out of sight. Suddenly her arms are wrapped around you. How did she get behind you?";
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
