package skills;

import status.Enthralled;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import combat.Combat;
import combat.Result;

public class EyesOfTemptation extends Skill {

	public EyesOfTemptation(Character self) {
		super("Tempt", self);
	}

	@Override
	public boolean requirements() {
		return requirements(self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Seduction) > 45 || user.getPure(Attribute.Dark) > 20 || user.getPure(Attribute.Arcane) > 10;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canRespond() && c.getStance().facing() && self.canSpend(30);
	}

	@Override
	public void resolve(Combat c, Character target) {
		Result result = target.roll(this, c, accuracy()+self.tohit())? Result.normal : Result.miss;
		if(self.human()) {
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()) {
			c.write(self,receive(c,0,Result.normal, target));
		}
		if (result == Result.normal) {
			target.add(new Enthralled(target, self));
			self.emote(Emotion.dominant, 50);
			self.spendMojo(30);
		}
	}

	@Override
	public Skill copy(Character user) {
		return new EyesOfTemptation(user);
	}
	public int speed(){
		return 9;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.normal)
			return Global.format("As {target:subject-action:gaze|gazes} into {self:name-possessive} eyes, {target:subject-action:feel|feels} {target:possessive} will slipping into the abyss.", self, target);
		else
			return Global.format("{target:SUBJECT-ACTION:look|looks} away as soon as {self:subject-action:focus|focuses} {self:possessive} eyes on {target:direct-object}", self, target);
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return deal(c, damage, modifier, target);
	}

	@Override
	public String describe() {
		return "Entralls your opponent with a single gaze.";
	}
}
