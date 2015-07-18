package skills;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;



public class TailJob extends Skill {

	public TailJob(Character self) {
		super("Tailjob", self);
	}

	@Override
	public boolean requirements(Character user) {
		boolean enough = getSelf().get(Attribute.Seduction)>=20 || getSelf().get(Attribute.Animism)>=0;
		return enough && user.body.get("tail").size() > 0;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&target.pantsless()&&c.getStance().mobile(getSelf())&&!c.getStance().mobile(target)&&!c.getStance().penetration(target);
	}

	@Override
	public String describe() {
		return "Use your tail to tease your opponent";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		int m = (5 + Global.random(10)) * (100 + getSelf().getArousal().percent())/100;
		String receiver;
		if (target.hasDick()) {
			receiver = "cock";
		} else {
			receiver = "pussy";
		}
		target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"), target.body.getRandom(receiver), m, c);
	}

	@Override
	public Skill copy(Character user) {
		return new TailJob(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (target.hasDick()) {
			return "You skillfully use your flexible " +getSelf().body.getRandom("tail").describe(getSelf()) + " to stroke and tease "+target.name()+"'s sensitive girl-cock.";
		} else {
			return "You skillfully use your flexible " +getSelf().body.getRandom("tail").describe(getSelf()) + " to stroke and tease "+target.name()+"'s sensitive girl parts.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if (target.hasDick()) {
			return getSelf().name()+" teases your sensitive dick and balls with her " + getSelf().body.getRandom("tail").describe(getSelf()) + ". It wraps completely around your shaft and strokes firmly.";
		} else {
			return getSelf().name()+" teases your sensitive pussy with her " + getSelf().body.getRandom("tail").describe(getSelf()) + ". It runs along your nether lips and leaves you gasping.";
		}
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
		return "tail";
	}
}
