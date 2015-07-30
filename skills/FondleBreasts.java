package skills;

import global.Global;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class FondleBreasts extends Skill {

	public FondleBreasts(Character self) {
		super("Fondle Breasts", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().reachTop(getSelf())&&target.hasBreasts()&&getSelf().canAct();
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 7;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int m = 1 + Global.random(4);
		if(target.roll(this, c, accuracy())){
			if(target.topless()){
				m += 4;
				if(getSelf().human()){
					c.write(getSelf(),deal(c,m,Result.normal, target));
				} else {
					c.write(getSelf(),receive(c,m,Result.normal, target));
				}
				target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("breasts"), m, c);					
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),deal(c,m,Result.normal, target));
				} else {
					c.write(getSelf(),receive(c,m,Result.normal, target));
				}
				target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("breasts"), m, c);					
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
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new FondleBreasts(user);
	}
	public int speed(){
		return 6;
	}
	public int accuracy(){
		return 7;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You grope at "+target.name()+"'s breasts, but miss.";
		}
		else if(target.top.isEmpty()){
			return "You massage "+target.name()+"'s soft breasts and pinch her nipples, causing her to moan with desire.";
		}
		else{
			return "You massage "+target.name()+"'s breasts over her "+target.top.peek().getName()+".";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return getSelf().name() + " gropes at your " + target.body.getRandomBreasts().describe(target) + ", but miss.";
		}
		else if(target.topless()){
			return getSelf().name() + " massages your " + target.body.getRandomBreasts().describe(target) + ", and pinch your nipples, causing you to moan with desire.";
		}
		else{
			return getSelf().name() + " massages your " + target.body.getRandomBreasts().describe(target) + " over your "+target.top.peek().getName()+".";
		}
	}

	@Override
	public String describe() {
		return "Grope your opponents breasts. More effective if she's topless";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		return "breasts";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "hands";
	}
}
