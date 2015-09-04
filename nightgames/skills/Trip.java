package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.StandingOver;
import nightgames.status.Braced;
import nightgames.status.Falling;
import nightgames.status.Stsflag;

public class Trip extends Skill {
	public Trip(Character self) {
		super("Trip", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().mobile(getSelf())&&!c.getStance().prone(target)&&c.getStance().front(getSelf())&&getSelf().canAct();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy(c)) && getSelf().check(Attribute.Cunning, target.knockdownDC())){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			target.add(c, new Falling(target));
		} else {
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
		return 10;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Cunning)>=16;
	}

	@Override
	public Skill copy(Character user) {
		return new Trip(user);
	}
	public int speed(){
		return 2;
	}
	public int accuracy(Combat c){
		return Math.round(Math.max(Math.min(150, 2.5f * (getSelf().get(Attribute.Cunning)
				- c.getOther(getSelf()).get(Attribute.Cunning)) + 75), 40));
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You try to trip "+target.name()+", but she keeps her balance.";
		} else {
			return "You catch "+target.name()+" off balance and trip her.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return getSelf().name()+" hooks your ankle, but you recover without falling.";
		} else {
			return getSelf().name()+" takes your feet out from under you and sends you sprawling to the floor.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Attempt to trip your opponent";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
