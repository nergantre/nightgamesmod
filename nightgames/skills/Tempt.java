package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Enthralled;

public class Tempt extends Skill {

	public Tempt(Character self) {
		super("Tempt", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canRespond();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
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
		if(getSelf().has(Trait.darkpromises)&& tempted && !target.wary() && getSelf().canSpend(15)) {
			getSelf().spendMojo(c, 15);
			c.write(getSelf(), Global.format("{self:NAME-POSSESSIVE} words fall on fertile grounds. {other:NAME-POSSESSIVE} will to resist crumbles in light of {self:possessive} temptation.", getSelf(), target));
			target.add(c, new Enthralled(target, getSelf(), 3));
		}

		target.tempt(c, getSelf(), n);
		target.emote(Emotion.horny,10);
		getSelf().emote(Emotion.confident, 10);
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
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
		return getSelf().temptLiner(c);
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().temptLiner(c);
	}

	@Override
	public String describe() {
		return "Tempts your opponent. More effective if they can see you.";
	}
}
