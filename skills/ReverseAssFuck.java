package skills;

import items.Item;
import stance.AnalCowgirl;
import status.Oiled;
import status.Stsflag;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;
import characters.body.BodyPart;

import combat.Combat;
import combat.Result;

public class ReverseAssFuck extends Fuck {
	public ReverseAssFuck(Character self) {
		super("Anal Ride", self, 0);
	}

	@Override
	public float priorityMod(Combat c) {
		return 0.0f + (self.getMood() == Emotion.dominant ? 1.0f : 0);
	}

	@Override
	public BodyPart getTargetOrgan(Character target) {
		return target.body.getRandomCock();
	}

	@Override
	public BodyPart getSelfOrgan() {
		return self.body.getRandom("ass");
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return fuckable(c, target)
				&&c.getStance().mobile(self)
				&&(c.getStance().prone(target)&&!c.getStance().mobile(target))
				&&self.canAct()&&!c.getStance().penetration(self)
				&&!c.getStance().penetration(target)
				&&(getTargetOrgan(target).isReady(target))
				&&(getSelfOrgan().isReady(target) || self.has(Item.Lubricant) || self.getArousal().percent()>50 || self.has(Trait.alwaysready));
	}

	@Override
	public void resolve(Combat c, Character target) {
		String premessage = "";
		if(!self.bottom.empty() && getSelfOrgan().isType("cock")) {
			if (self.bottom.size() == 1) {
				premessage += String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s", self.bottom.get(0).name());
			} else if (self.bottom.size() == 2) {
				premessage += String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s and %s", self.bottom.get(0).name(), self.bottom.get(1).name());
			}
		}

		premessage = Global.format(premessage, self, target);
		if(!self.hasStatus(Stsflag.oiled)&&self.getArousal().percent()>50 || self.has(Trait.alwaysready)) {
			String fluids = self.hasDick() ? "copious pre-cum" : "own juices";
			if (premessage.isEmpty()) {
				premessage = "{self:subject-action:lube|lubes}";
			} else {
				premessage += " and {self:action:lube|lubes}";
			}
			premessage += " up {self:possessive} ass with {self:possessive} " + fluids + ".";
			self.add(new Oiled(self));
		} else if(!self.hasStatus(Stsflag.oiled)&&self.has(Item.Lubricant)) {
			if (premessage.isEmpty()) {
				premessage = "{self:subject-action:lube|lubes}";
			} else {
				premessage += " and {self:action:lube|lubes}";
			}
			premessage += " up {self:possessive} ass.";
			self.add(new Oiled(self));
			self.consume(Item.Lubricant, 1);
		}
		c.write(self, Global.format(premessage, self, target));
	
		int m = Global.random(5);
		if(self.human()) {
			c.write(self,deal(c,m,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,m,Result.normal, target));
		}

		c.setStance(new AnalCowgirl(self,target));
		target.body.pleasure(self, getSelfOrgan(), getTargetOrgan(target), m, c);		
		self.body.pleasure(target, getTargetOrgan(target), getSelfOrgan(), m / 2, c);
		self.buildMojo(c, 25);
		self.emote(Emotion.dominant, 30);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Seduction)>=20;
	}

	@Override
	public Skill copy(Character user) {
		return new ReverseAssFuck(user);
	}
	public int speed(){
		return 2;
	}
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return String.format("You make sure your %s is sufficiently lubricated and you push %s %s into your greedy hole.",
				getSelfOrgan().describe(self), target.nameOrPossessivePronoun(), getTargetOrgan(target).describe(target));
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return String.format("%s makes sure her %s is sufficiently lubricated and pushes %s %s into her greedy hole.",
				self.name(), getSelfOrgan().describe(self), target.nameOrPossessivePronoun(), getTargetOrgan(target).describe(target));
	}

	@Override
	public String describe() {
		return "Fuck your opponent with your ass.";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
