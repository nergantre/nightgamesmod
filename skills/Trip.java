package skills;

import stance.StandingOver;
import status.Braced;
import status.Stsflag;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Trip extends Skill {
	public Trip(Character self) {
		super("Trip", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().mobile(getSelf())&&!c.getStance().prone(target)&&c.getStance().front(getSelf())&&getSelf().canAct();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy()) && getSelf().check(Attribute.Cunning, target.knockdownDC())){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			if(c.getStance().prone(getSelf())&&!getSelf().is(Stsflag.braced)){
				getSelf().add(new Braced(getSelf()));
			}
			c.setStance(new StandingOver(getSelf(),target));
		} else {
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
		}
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Cunning)>=16;
	}

	@Override
	public Skill copy(Character user) {
		return new Trip(user);
	}
	public int speed(){
		return 2;
	}
	public int accuracy(){
		return 2;
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
	public String describe() {
		return "Attempt to trip your opponent";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
