package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Mount;

public class Straddle extends Skill {

	public Straddle(Character self) {
		super("Mount", self);
		
	}

	@Override
	public boolean usable(Combat c, Character target) {
		
		return c.getStance().mobile(getSelf())&&c.getStance().mobile(target)&&c.getStance().prone(target)&&getSelf().canAct();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		c.setStance(new Mount(getSelf(),target));
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Straddle(user);
	}
	public int speed(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You straddle "+target.name()+" using your body weight to hold her down.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" plops herself down on top of your stomach.";
	}

	@Override
	public String describe(Combat c) {
		return "Straddles opponent";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
