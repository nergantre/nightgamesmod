package skills;

import global.Global;
import stance.FlyingCarry;
import stance.FlyingCowgirl;
import stance.StandingOver;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.body.BodyPart;
import combat.Combat;
import combat.Result;

public class ReverseFly extends Fly {
	public ReverseFly(Character self) {
		super("ReverseFly", self);
	}

	@Override
	public String describe() {
		return "Take off and fuck your opponent's cock in the air.";
	}

	@Override
	public Skill copy(Character target) {
		return new ReverseFly(target);
	}

	@Override
	public BodyPart getSelfOrgan() {
		return getSelf().body.getRandomPussy();
	}

	@Override
	public BodyPart getTargetOrgan(Character target) {
		return target.body.getRandomCock();
	}

	@Override
	public void resolve(Combat c, Character target) {
		String premessage = "";
		getSelf().spendMojo(c, getMojoSpent());
		if (getSelf().bottom.size() == 1) {
			premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s to the side and", getSelf().bottom.get(0).name());
		} else if (getSelf().bottom.size() == 2) {
			premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s and %s to the side and", getSelf().bottom.get(0).name(), getSelf().bottom.get(1).name());
		}

		premessage = Global.format(premessage, getSelf(), target);
		Result result = target.roll(this, c, accuracy()) ? Result.normal: Result.miss;
		if (this.getSelf().human()) {
			c.write(getSelf(),premessage + deal(c, 0, result, target));
		} else if (target.human()) {
			c.write(getSelf(),premessage + receive(c, 0, result, this.getSelf()));
		}
		if (result == Result.normal) {
			getSelf().emote(Emotion.dominant,50);
			getSelf().emote(Emotion.horny, 30);
			target.emote(Emotion.desperate, 50);
			target.emote(Emotion.nervous, 75);
			c.setStance(new FlyingCowgirl(this.getSelf(), target));
		} else {
			c.setStance(new StandingOver(target, getSelf()));
		}
	}

	@Override
	public String deal(Combat c, int amount, Result modifier, Character target) {
		if (modifier == Result.miss){
			return "you grab " + target.name() + " tightly and try to take off. However " +target.pronoun() + " has other ideas. She knees your crotch as you approach and sends you sprawling to the ground.";
		} else {
			return "you grab " + target.name() + " tightly and take off, "
					+ "inserting his dick into your hungry " + getSelf().body.getRandomPussy().describe(getSelf()) + ".";
		}
	}

	@Override
	public String receive(Combat c, int amount,
			Result modifier, Character target) {
		if (modifier == Result.miss){
			return target.name() + " lunges for you with a hungry look in her eyes. However you have other ideas. You trip her as she approaches and send her sprawling to the floor.";
		} else {
			return "suddenly, " + getSelf().name() + " leaps at you, embracing you tightly"
					+ ". She then flaps her " + getSelf().body.getRandomWings().describe(target) + " hard and before you know it"
					+ " you are twenty feet in the sky held up by her arms and legs."
					+ " Somehow, your dick ended up inside of her in the process and"
					+ " the rythmic movements of her flying arouse you to no end";
		}
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
