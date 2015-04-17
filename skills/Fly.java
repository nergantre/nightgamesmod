package skills;

import characters.Attribute;
import characters.Character;
import characters.Emotion;
import combat.Combat;
import stance.FlyingCarry;
import combat.Result;

public class Fly extends Fuck {
	public Fly(Character self) {
		super("Fly", self);
	}

	public Fly(String name, Character self) {
		super(name, self);
	}

	@Override
	public boolean requirements() {
		return (this.self.body.get("wings").size() > 0) && self.getPure(Attribute.Power)>=15;
	}

	@Override
	public boolean requirements(Character user) {
		return (user.body.get("wings").size() > 0) && user.getPure(Attribute.Power)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return fuckable(c, target)
				&& !target.wary()
				&& (this.self.canAct())
				&& (c.getStance().mobile(this.self))
				&& (!c.getStance().prone(this.self))
				&& (this.self.getStamina().get() >= 15)
				&& (this.self.canSpend(getMojoSpent()))
				&& (!c.getStance().penetration(this.self));
	}
	
	public int getMojoSpent() {
		return 35;
	}

	@Override
	public String describe() {
		return "Take off and fuck your opponent's pussy in the air.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if (this.self.human()) {
			c.write(self,deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(self,receive(c, 0, Result.normal, this.self));
		}
		self.spendMojo(getMojoSpent());
		self.emote(Emotion.dominant,50);
		self.emote(Emotion.horny, 30);
		target.emote(Emotion.desperate, 50);
		target.emote(Emotion.nervous, 75);
		c.setStance(new FlyingCarry(this.self, target));
	}

	@Override
	public Skill copy(Character target) {
		return new Fly(target);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int amount,
			Result modifier, Character target) {
		return "You grab " + target.name() + " tightly and take off, "
				+ (target.hasDick()&&self.hasPussy() ? "inserting her dick into your hungry " + self.body.getRandomPussy().describe(self) + "." :
					 " holding her helpless in the air and thrusting deep into her wet " + target.body.getRandomPussy().describe(self) + ".");
	}

	@Override
	public String receive(Combat c, int amount,
			Result modifier, Character target) {
		return "Suddenly, " + self.name() + " leaps at you, embracing you tightly"
				+ ". She then flaps her " + self.body.getRandomWings().describe(target) + " hard and before you know it"
				+ " you are twenty feet in the sky held up by her arms and legs."
				+ " Somehow, her dick ended up inside of you in the process and"
				+ " the rythmic movements of her flying arouse you to no end";
	}
}
