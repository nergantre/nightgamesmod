package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.BodyFetish;

public class FootSmother extends Skill {
	public FootSmother(Character self) {
		super("Foot Smother", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Fetish) >= 20;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().footAvailable()&&getSelf().body.has("feet")&&getSelf().canAct()&&c.getStance().prone(target)&&!c.getStance().behind(getSelf());
	}

	public int accuracy(Combat c){
		return 150;
	}
	@Override
	public boolean resolve(Combat c, Character target) {
		Result result = Result.normal;
		int m = 0;
		m = 8 + Global.random(6);
		if (getSelf().human()) {
			c.write(getSelf(), Global.format(deal(c, 0, Result.normal, target), getSelf(), target));
		} else {
			c.write(getSelf(), Global.format(receive(c, 0, Result.normal, target), getSelf(), target));
		}
		target.tempt(c, getSelf(), getSelf().body.getRandom("feet"), m);
		if (Global.random(100) < 30 + 2 * getSelf().get(Attribute.Fetish)) {
			target.add(c, new BodyFetish(target, getSelf(), "feet", .25));
		}
		return result != Result.miss;
	}

	@Override
	public Skill copy(Character user) {
		return new FootSmother(user);
	}
	public int speed(){
		return 2;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You place the soles of your feet over top of {other:name-possessive} face and press down, keeping {other:direct-object} in place and giving {other:direct-object} no choice but to worship your feet.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		String message = "{self:SUBJECT} presses {self:possessive} soles in to your face causing you to inhale {self:possessive} scent deeply. As you start to worship {self:possessive} {self:body-part:feet}, ";
		String parts = "";
		if (target.hasDick()) {
			if (target.getArousal().percent() < 30) {
				parts += "your {other:body-part:cock} starts to twitch";
			} else if (target.getArousal().percent() < 60) {
				parts += "your {other:body-part:cock} starts to throb";
			} else {
				parts += "your {other:body-part:cock} start to leak " + target.body.getRandomCock().getFluids(target);
			}
		}
		if (target.hasPussy()) {
			if (parts.length() > 0) {
				parts += " and ";
			}
			if (target.getArousal().percent() < 30) {
				parts += "you feel yourself start to get wet";
			} else if (target.getArousal().percent() < 60) {
				parts += "you feel your wetness start to run down your leg";
			} else {
				parts += "your {other:body-part:pussy} starts to spasm as your " +target.body.getRandomPussy().getFluids(target) + " puddles underneath you";
			}
		}
		return message + parts + ".";
	}

	@Override
	public String describe(Combat c) {
		return "Smothers your opponent's face with your foot. Low damage but high chance of inducing fetishes.";
	}
}
