package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.BehindFootjob;
import nightgames.stance.Stance;

public class FootPump extends Skill {
	public FootPump(Character self) {
		super("Foot Pump", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Seduction)>=22;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (c.getStance().behind(getSelf())&&target.pantsless()&&getSelf().canAct()&&!c.getStance().penetration(target)&&target.hasDick());
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
		if (target.hasDick())
			target.body.pleasure(getSelf(), getSelf().body.getRandom("feet"), target.body.getRandom("cock"), m, c);
		else
			target.body.pleasure(getSelf(), getSelf().body.getRandom("feet"), target.body.getRandom("pussy"), m, c);
		target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("breasts"), m2, c);
		if (c.getStance().en != Stance.behindfootjob) {
			c.setStance(new BehindFootjob(getSelf(), target));
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
		return receive(c, damage, modifier, target);
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return Global.format("{other:SUBJECT-ACTION:feel|feels} {self:direct-object} wrap {self:possessive} long legs around {other:possessive} waist and settle {self:possessive} {self:body-part:feet} on either side of {other:possessive} {other:body-part:cock}.	Cupping {other:possessive} dick with {self:possessive} arches, {self:subject-action:starts|start} making long steady strokes by moving {self:possessive} legs up and down. Reaching around {other:direct-object}, {self:subject-action:also start|also starts} to rub and gently flick {other:possessive} nipples with {self:possessive} fingers. Alternating between pumping and massaging {other:possessive} glans with {self:possessive} toes, {self:subject-action:quickly make|quickly makes} {other:name-do} gasp in pleasure.", getSelf(), target);
	}

	@Override
	public String describe() {
		return "Pleasure your opponent with your feet";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		return "cock";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "feet";
	}
}
