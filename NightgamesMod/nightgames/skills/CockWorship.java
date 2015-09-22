package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.ReverseMount;
import nightgames.stance.SixNine;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.CockBound;
import nightgames.status.Stsflag;
import nightgames.status.Trance;

public class CockWorship extends Skill {

	public CockWorship(Character self) {
		super("Cock Worship", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (target.crotchAvailable()&&target.hasDick()&&c.getStance().oral(getSelf())&&c.getStance().front(getSelf())&&getSelf().canAct()&&!c.getStance().vaginallyPenetrated(target));
	}

	@Override
	public float priorityMod(Combat c) {
		return 0;
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 0;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int m = 10 + Global.random(8);
		if(getSelf().has(Trait.silvertongue)){
			m += 4;
		}
		if(target.human()){
			c.write(getSelf(),receive(c,m,Result.normal, target));
		}
		else if(getSelf().human()){
			c.write(getSelf(),deal(c,m,Result.normal, target));
		}
		BodyPart mouth = getSelf().body.getRandom("mouth");
		BodyPart cock = target.body.getRandom("cock");
		target.body.pleasure(getSelf(), mouth, cock, m, c);
		if (getSelf().hasDick() && (!getSelf().hasPussy() || Global.random(2) == 0)) {
			getSelf().body.pleasure(getSelf(), getSelf().body.getRandom("hands"), getSelf().body.getRandomCock(), m, c);
		} else if (getSelf().hasPussy()){
			getSelf().body.pleasure(getSelf(), getSelf().body.getRandom("hands"), getSelf().body.getRandomPussy(), m, c);
		} else {
			getSelf().body.pleasure(getSelf(), getSelf().body.getRandom("hands"), getSelf().body.getRandomHole(), m, c);
		}
		if (mouth.isErogenous()) {
			getSelf().body.pleasure(getSelf(), cock, mouth, m, c);
		}

		target.buildMojo(c, 20);
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		Optional<BodyFetish> fetish = getSelf().body.getFetish("cock");
		return fetish.isPresent() && fetish.get().magnitude >= .5;
	}

	public int accuracy(Combat c){
		return 150;
	}
	@Override
	public Skill copy(Character user) {
		return new CockWorship(user);
	}
	public int speed(){
		return 2;
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return Global.format("You ecstatically crawl towards {other:name-do} and reverently hold {other:possessive} {other:body-part:cock} with your hands. "
				+ "You carefully take {other:possessive} member into your {self:body-part:mouth} and start blowing {other:direct-object} for all you are worth. "
				+ "Minutes pass and you lose yourself in sucking {other:name-possessive} divine shaft while idly playing with yourself. Finally, {other:subject} "
				+ "pushes your head away from {other:possessive} cock and you finally regain your senses.", getSelf(), target);
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return Global.format("{self:subject} ecstatically crawls to you on {self:possessive} knees and reverently cups {other:possessive} {other:body-part:cock}"
				+ "with {self:possessive} hands. She carefully takes {other:possessive} member into {other:possessive} {self:body-part:mouth} and start sucking on it "
				+ "like it was the most delicious popsicle made. Minutes pass and {self:subject} continues blowing {other:possessive} shaft while idly playing with "
				+ "{self:reflective}. Feeling a bit too good, you manage to push {self:name-do} away from your cock lest she makes you cum accidentally.", getSelf(), target);	}

	@Override
	public String describe(Combat c) {
		return "Worship your opponent's dick";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
