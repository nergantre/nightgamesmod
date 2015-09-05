package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.BodyFetish;

public class Footjob extends Skill {

	public Footjob(Character self) {
		super("Footjob", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction)>=22;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().feet(getSelf())&&target.crotchAvailable()&&(c.getStance().prone(getSelf())!=c.getStance().prone(target))&&getSelf().canAct()&&!c.getStance().penetration(target);
	}

	@Override
	public float priorityMod(Combat c) {
		BodyPart feet = getSelf().body.getRandom("feet");
		Character other = c.p1 == getSelf() ? c.p2 : c.p1;
		BodyPart otherpart = other.hasDick() ? other.body.getRandomCock() : other.body.getRandomPussy();
		if (feet != null) {
			return (float) Math.max(0, (feet.getPleasure(getSelf(), otherpart) - 1));
		}
		return 0;
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 15;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy(c))){
			int m = 8 + Global.random(6);
			if(getSelf().human()){
				c.write(getSelf(),deal(c,m,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,m,Result.normal, target));
			}
			if (target.hasDick())
				target.body.pleasure(getSelf(), getSelf().body.getRandom("feet"), target.body.getRandom("cock"), m, c);
			else
				target.body.pleasure(getSelf(), getSelf().body.getRandom("feet"), target.body.getRandom("pussy"), m, c);
			if (Global.random(100) < 15 + 2 * getSelf().get(Attribute.Fetish)) {
				target.add(c, new BodyFetish(target, getSelf(), "feet", .25));
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
	public Skill copy(Character user) {
		return new Footjob(user);
	}
	public int speed(){
		return 4;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You aim your foot between "+target.name()+"'s legs, but miss.";
		}
		else if(target.hasDick()){
			return "You press your foot against "+target.name()+"'s girl-cock and stimulate it by grinding it with the sole.";
		}
		else{
			return "You rub your foot against "+target.name()+"'s pussy lips, using her own wetness as lubricant, and stimulate her love button with your toe.";
		}
		
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character attacker) {
		if(modifier==Result.miss){
			return getSelf().name()+" swings her foot at your groin, but misses.";
		}
		else{
			return getSelf().name()+" rubs your dick with the sole of her soft foot. From time to time, she teases you by pinching the glans between her toes and jostling your balls.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Pleasure your opponent with your feet";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		if (target.hasDick()) {
			return "cock";
		} else {
			return "pussy";
		}
	}
	public String getWithOrganType(Combat c, Character target) {
		return "feet";
	}
}
