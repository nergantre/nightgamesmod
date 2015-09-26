package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.stance.StandingOver;
import nightgames.status.Bound;
import nightgames.status.Falling;

public class DarkTendrils extends Skill {

	public DarkTendrils(Character self) {
		super("Dark Tendrils", self, 4);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Dark)>=12;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && !c.getStance().sub(getSelf())&&!c.getStance().prone(getSelf())&&!c.getStance().prone(target)&&getSelf().canAct();
	}

	@Override
	public String describe(Combat c) {
		return "Summon shadowy tentacles to grab or trip your opponent: 5 Arousal";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().arouse(5, c);
		if(target.roll(this, c, accuracy(c))){
			if(Global.random(2)==1){
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.normal, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.normal, target));
				}
				target.add(c, new Bound(target,Math.min(10+3*getSelf().get(Attribute.Dark), 55),"shadows"));
			} else if(getSelf().check(Attribute.Dark,target.knockdownDC()-getSelf().getMojo().get())){
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.weak, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.weak, target));
				}
				target.add(c, new Falling(target));
			} else {
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.miss, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.miss, target));
				}
			}
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
	public nightgames.skills.Skill copy(Character user) {
		return new DarkTendrils(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	public int accuracy(Combat c){
		return 75;
	}
	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You summon dark tentacles to hold "+target.name()+", but she twists away.";
		}
		else  if(modifier == Result.weak){
			return "You summon dark tentacles that take "+target.name()+" feet out from under her.";
		}
		else{
			return "You summon a mass of shadow tendrils that entangle "+target.name()+" and pin her arms in place.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return getSelf().name()+" makes a gesture and evil looking tentacles pop up around you. You dive out of the way as they try to grab you.";
		}
		else  if(modifier == Result.weak){
			return "Your shadow seems to come to life as dark tendrils wrap around your legs and bring you to the floor.";
		}
		else{
			return getSelf().name()+" summons shadowy tentacles that snare your arms and hold you in place.";
		}
	}

}
