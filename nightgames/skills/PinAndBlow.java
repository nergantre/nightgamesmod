package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.BehindFootjob;
import nightgames.stance.HeldOral;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;

public class PinAndBlow extends Skill {
	public PinAndBlow(Character self) {
		super("Oral Pin", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction)>=22&&user.get(Attribute.Power) >= 15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (c.getStance().prone(target)&&target.pantsless()&&getSelf().canAct()&&!c.getStance().penetration(target))&&c.getStance().en!=Stance.oralpin;
	}

	@Override
	public float priorityMod(Combat c) {
		return 0;
	}

	@Override
	public int getMojoCost(Combat c) {
		return 20;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		Result res = Result.normal;

		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,res, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,res, target));
		}
		if (res != Result.miss) {
			c.setStance(new HeldOral(getSelf(), target));
			if (target.hasDick())
				(new Blowjob(getSelf())).resolve(c, target);
			else if (target.hasPussy())
				(new Cunnilingus(getSelf())).resolve(c, target);
			else if (target.body.has("ass"))
				(new Anilingus(getSelf())).resolve(c, target);
			return true;
		}
		return false;
	}

	@Override
	public Skill copy(Character user) {
		return new PinAndBlow(user);
	}
	public int speed(){
		return 5;
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
		return Global.format("{self:SUBJECT-ACTION:bow|bows} {other:name-do} over, and {self:action:settle|settles} {self:possessive} head between {other:possessive} legs.", getSelf(), target);
	}

	@Override
	public String describe(Combat c) {
		return "Holds your opponent down and use your mouth";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		return "none";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "mouth";
	}
}
