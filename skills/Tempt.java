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
	public boolean requirements() {
		return requirements(self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canRespond();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()) {
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()) {
			c.write(self,receive(c,0,Result.normal, target));
		}
		double m = (int) Math.round(4+Global.random(4));
		if (!c.getStance().behind(self)) {
			// opponent can see self
			m += 3 * self.body.getCharismaBonus(target);
		}
		if (target.has(Trait.imagination)) {
			m *= 1.5;
		}

		int n = (int)Math.round(m);

		boolean tempted = Global.random(5) == 0;
		if(self.has(Trait.darkpromises)&& tempted && self.canSpend(15) && !target.wary()){
			self.spendMojo(c, 15);
			c.write(self, Global.format("{self:NAME-POSSESSIVE} words fall on fertile grounds. {other:NAME-POSSESSIVE} will to resist crumbles in light of {self:possessive} temptation.", self, target));
			target.add(new Enthralled(target, self, 3));
		}

		target.tempt(c, self, n);
		target.emote(Emotion.horny,10);
		self.emote(Emotion.confident, 10);
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Seduction) > 15;
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
		return self.temptLiner(target);
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.temptLiner(target);
	}

	@Override
	public String describe() {
		return "Tempts your opponent. More effective if they can see you.";
	}
}
