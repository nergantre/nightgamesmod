package skills;

import status.Enthralled;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Tempt extends Skill {

	public Tempt(Character self) {
		super("Tempt", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canRespond();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(getSelf().human()) {
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()) {
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		double m = (int) Math.round(4+Global.random(4));
		if (c.getStance().front(getSelf())) {
			// opponent can see self
			m += 3 * getSelf().body.getCharismaBonus(target);
		}
		if (target.has(Trait.imagination)) {
			m *= 1.5;
		}

		int n = (int)Math.round(m);

		boolean tempted = Global.random(5) == 0;
		if(getSelf().has(Trait.darkpromises)&& tempted && getSelf().canSpend(15) && !target.wary()){
			getSelf().spendMojo(c, 15);
			c.write(getSelf(), Global.format("{self:NAME-POSSESSIVE} words fall on fertile grounds. {other:NAME-POSSESSIVE} will to resist crumbles in light of {self:possessive} temptation.", getSelf(), target));
			target.add(new Enthralled(target, getSelf(), 3));
		}

		target.tempt(c, getSelf(), n);
		target.emote(Emotion.horny,10);
		getSelf().emote(Emotion.confident, 10);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Seduction) > 15;
	}

	@Override
	public Skill copy(Character user) {
		return new Tempt(user);
	}
	public int speed(){
		return 9;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return getSelf().temptLiner(target);
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().temptLiner(target);
	}

	@Override
	public String describe() {
		return "Tempts your opponent. More effective if they can see you.";
	}
}
