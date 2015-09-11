package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Pin;

public class Restrain extends Skill {

	public Restrain(Character self) {
		super("Pin", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().mobile(getSelf())&&c.getStance().prone(target)&&c.getStance().reachTop(getSelf())&&getSelf().canAct()&&c.getStance().reachTop(target)&&!c.getStance().connected()&&!c.getStance().penetration(target);
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		return resolve(c, target, false);
	}

	public boolean resolve(Combat c, Character target, boolean nofail) {
		if(nofail || target.roll(this, c, accuracy(c))) {
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			c.setStance(new Pin(getSelf(),target));
			target.emote(Emotion.nervous, 10);
			target.emote(Emotion.desperate, 10);
			getSelf().emote(Emotion.dominant, 20);
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
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Power)>=8;
	}

	@Override
	public Skill copy(Character user) {
		return new Restrain(user);
	}
	public int speed(){
		return 2;
	}
	public int accuracy(Combat c){
		return 75;
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You try to catch "+target.name()+"'s hands, but she squirms too much to keep your grip on her.";
		}
		else{
			return "You manage to restrain "+target.name()+", leaving her helpless and vulnerable beneath you.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return getSelf().name()+" tries to pin you down, but you keep your arms free.";
		}
		else{
			return getSelf().name()+" pounces on you and pins your arms in place, leaving you at her mercy.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Restrain opponent until she struggles free";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
