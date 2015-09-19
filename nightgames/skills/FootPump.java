package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.BehindFootjob;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;

public class FootPump extends Skill {
	public FootPump(Character self) {
		super("Foot Pump", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction)>=22;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (c.getStance().behind(getSelf())&&target.crotchAvailable()&&getSelf().canAct()&&!c.getStance().inserted()&&target.hasDick())&&getSelf().outfit.hasNoShoes();
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
		return 20;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int m = 12 + Global.random(6);
		int m2 = m / 2;
		if(getSelf().human()){
			c.write(getSelf(),deal(c,m,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,m,Result.normal, target));
		}
		target.body.pleasure(getSelf(), getSelf().body.getRandom("feet"), target.body.getRandom("cock"), m, c);
		target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("breasts"), m2, c);
		if (c.getStance().en != Stance.behindfootjob) {
			c.setStance(new BehindFootjob(getSelf(), target));
		}
		if (Global.random(100) < 15 + 2 * getSelf().get(Attribute.Fetish)) {
			target.add(c, new BodyFetish(target, getSelf(), "feet", .25));
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new FootPump(user);
	}
	public int speed(){
		return 4;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return Global.format("You wrap your legs around {other:name-possessive} waist and grip {other:possessive} {other:body-part:cock} between your toes. Massaging {other:name-possessive} {other:body-part:cock} between your toes, you start to stroke {other:possessive} {other:body-part:cock} up and down between your toes. Reaching around from behind {other:possessive} back, you start to tease and caress {other:possessive} breasts with your hands. Alternating between pumping and massaging the head of {other:possessive} {other:body-part:cock} with your toes, {other:pronoun} begins to let out a low moan with each additional touch.", getSelf(), target);
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return Global.format("{self:SUBJECT} wraps {self:possessive} legs around your waist and settles {self:possessive} feet on both sides of your {other:body-part:cock}. Cupping your dick with {self:possessive} arches, she starts making long and steady strokes up and down your {other:body-part:cock} as it remains trapped in between {self:possessive} arches. Reaching around you, {self:subject} begins to rub and gently flick your nipples with {self:possessive} fingers. Alternating between pumping and massaging the head of your {other:body-part:cock} with {self:possessive} toes you can’t help but groan in pleasure.", getSelf(), target);
	}
	@Override
	public String describe(Combat c) {
		return "Pleasure your opponent with your feet";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
