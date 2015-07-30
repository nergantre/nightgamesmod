package nightgames.characters.body;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.CockBound;
import nightgames.status.Enthralled;
import nightgames.status.Horny;
import nightgames.status.Shamed;
import nightgames.status.Stsflag;

import java.io.PrintWriter;
import java.util.Scanner;

public enum PussyPart implements BodyPart {
	normal("", 0, 1, 1, 6, 15, 0),
	arcane("arcane patterned ", .2, 1.1, 1, 9, 5, 3),
	fiery("fiery ", 0, 1.3, 1.2, 8, 15, 3),
	succubus("succubus ", .6, 1.5, 1.2, 999, 0, 4),
	feral("feral ", 1, 1.3, 1.2, 8, 7, 2),
	cybernetic("cybernetic ", -.50, 1.8, .5, 200, 0, 4),
	gooey("gooey ", .4, 1.5, 1.2, 999, 0, 6),
	tentacled("tentacled ", 0, 2, 1.2, 999, 0, 8);

	public double priority;
	public String desc;
	public String type;
	public double hotness;
	public double pleasure;
	public double capacity;
	public double sensitivity;
	public int wetThreshold;
	public static String synonyms[] = {
		"pussy", "vagina", "slit",
	};
	PussyPart(String desc, double hotness, double pleasure, double sensitivity, double capacity, int wetThreshold, int priority) {
		this(desc, hotness,pleasure,sensitivity,capacity,wetThreshold, priority, "pussy");
	}
	PussyPart(String desc, double hotness, double pleasure, double sensitivity, double capacity, int wetThreshold, int priority, String type) {
		this.type = type;
		this.desc = desc;
		this.hotness = hotness;
		this.pleasure = pleasure;
		this.capacity = capacity;
		this.sensitivity = sensitivity;
		this.wetThreshold = wetThreshold;
		this.priority = priority;
	}

	@Override
	public void describeLong(StringBuilder b, Character c) {
		b.append("A ");
		if (c.getArousal().percent() > 15 && c.getArousal().percent() < 60) {
			b.append("moist ");
		} else if (c.getArousal().percent() >= 60) {
			b.append("drenched ");
		}
		b.append(describe(c));
		b.append(" ");
		if (isType("pussy"))
			b.append("is nested between " + c.nameOrPossessivePronoun() + " legs.");
		else if (isType("ass"))
			b.append("is nested between " + c.nameOrPossessivePronoun() + " asscrack.");
	}

	@Override
	public double priority(Character c) {
		return priority + (c.has(Trait.tight) ? 1 : 0)+ (c.has(Trait.holecontrol) ? 1 : 0) + + (c.has(Trait.autonomousPussy) ? 4 : 0);
	}

	@Override
	public String describe(Character c) {
		String syn = Global.pickRandom(synonyms);
		return desc + syn;
	}

	@Override
	public String fullDescribe(Character c) {
		String syn = Global.pickRandom(synonyms);
		return desc + syn;
	}

	@Override
	public String toString() {
		return desc;
	}

	public boolean isType(String type) {
		return type.equalsIgnoreCase(this.type);
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public double getHotness(Character self, Character opponent) {
		double val = hotness;
		if (!opponent.hasDick())
			val /= 2;
		return val;
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		double pleasureMod = pleasure;
		pleasureMod += self.has(Trait.pussyTraining1) ? .5 : 0;
		pleasureMod += self.has(Trait.pussyTraining2) ? .7 : 0;
		pleasureMod += self.has(Trait.pussyTraining3) ? .7 : 0;
		return pleasureMod;
	}

	@Override
	public double getSensitivity(BodyPart target) {
			return sensitivity;
	}

	public boolean isReady(Character self) {
		return self.has(Trait.alwaysready) || self.getArousal().percent() >= wetThreshold;
	}

	@Override
	public void save(PrintWriter saver) {
		saver.write(this.name());
	}

	@Override
	public BodyPart load(Scanner loader) {
		return PussyPart.valueOf(loader.nextLine());
	}

	@Override
	public double applyReceiveBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (this==PussyPart.feral) {
			c.write(self, String.format("Musk emanating from %s %s leaves %s reeling.",
					self.possessivePronoun(), describe(self), opponent.directObject()));
			opponent.add(c, new Horny(opponent, (int) Math.max(1, Math.floor(damage/5)), 5, self.nameOrPossessivePronoun() + " feral musk"));
		}
		if (opponent.has(Trait.pussyhandler)) {
			c.write(opponent, Global.format("{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} pussy causes {self:subject} to shudder uncontrollably.",
					self, opponent));
			bonus += 5;
		}
		return bonus;
	}
	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (this==PussyPart.succubus && target.isType("cock")) {
			c.write(self, String.format("%s hot flesh kneads %s %s as %s %s"
										+", drawing gouts of life energy out of %s %s, which is greedily absorbed by %s %s",
					self.possessivePronoun(), opponent.possessivePronoun(), target.describe(opponent), self.subjectAction("ride", "rides"), opponent.directObject(),
					opponent.possessivePronoun(), target.describe(opponent),  self.possessivePronoun(), describe(self)));
			int strength = 10 + self.get(Attribute.Dark)/2;
			opponent.weaken(c, strength);
			self.heal(c, strength);
			for (int i = 0; i < 10; i++) {
				Attribute stolen = (Attribute) opponent.att.keySet().toArray()[Global.random(opponent.att.keySet().size())];
				if (stolen != Attribute.Perception && opponent.get(stolen) > 0) {
					int stolenStrength = Math.min(strength / 10, opponent.get(stolen));
					opponent.add(c, new Abuff(opponent, stolen, -stolenStrength, 20));
					self.add(c, new Abuff(self, stolen, stolenStrength, 20));
					break;
				}
			}
		}
		if (this==PussyPart.tentacled && target.isType("cock")) {
			if (!opponent.is(Stsflag.cockbound)) {
				if (!self.human()) {
					c.write(self, Global.format("Deep inside {self:name-possessive} pussy, soft walls pulse and strain against your cock. "
												+"You suddenly feel hundreds of thin tentacles, probing like fingers, dancing over every inch of your pole. " +
												"A thicker tentacle wraps around your cock, preventing any escape", self, opponent));
				} else {
					c.write(self, Global.format("As {other:name-possessive} cock pumps into you, you focus your mind on your lower entrance. "
												+"You mentally command the tentacles inside your womb to constrict and massage {other:possessive} cock. " +
												"{other:name} almost starts hyperventilating from the sensations.", self, opponent));
				}
			opponent.add(c, new CockBound(opponent, 10, self.nameOrPossessivePronoun() + " vaginal tentacles"));
			} else {
				if (!self.human()) {
					c.write(self, Global.format("As you thrust into {self:name-possessive} pussy, hundreds of tentacles squirms against the motions of your cock, " +
												"making each motion feel like it will push you over the edge.", self, opponent));
				} else {
					c.write(self, Global.format("As {other:name-possessive} cock pumps into you, your pussy tentacles reflexively curl around the intruding object, rhythmically" +
							"squeezing and milking it constantly.", self, opponent));
				}
				bonus += 5 + Global.random(4);
			}
		}
		if (this==PussyPart.cybernetic && target.isType("cock") && Global.random(3) == 0) {
			c.write(self, String.format("%s %s whirls to life and starts attempting to extract all the semen packed inside %s %s. "
										+"At the same time, %s feel a thin filament sliding into %s urethra, filling %s with both pleasure and shame.",
					self.possessivePronoun(), describe(self), opponent.possessivePronoun(), target.describe(opponent),
					opponent.pronoun(), opponent.possessivePronoun(), opponent.directObject()));
			bonus += 15;
			opponent.add(c, new Shamed(opponent));
		}
		if (this==PussyPart.fiery && target.isType("cock")) {
			c.write(self, String.format("Pluging %s %s into %s %s leaves %s gasping from the heat.",
					opponent.possessivePronoun(), target.describe(opponent), self.possessivePronoun(), describe(self),
					opponent.directObject()));
			opponent.pain(c, 20 + self.get(Attribute.Ki)/2);
		}
		if (this==PussyPart.arcane) {
			String message = self.nameOrPossessivePronoun() + " tattoos surrounding " + self.possessivePronoun() + " vagina lights up with arcane energy as " + opponent.subjectAction("are", "is") + " inside " + opponent.directObject() +", channeling some of " + opponent.possessivePronoun() + " energies back to its master.";
			int strength = 5 + self.get(Attribute.Arcane)/6;
			opponent.loseMojo(c, strength);
			self.buildMojo(c, strength);
			if (Global.random(8) == 0 && !opponent.wary()) {
				opponent.add(c, new Enthralled(opponent, self, 3));
				message += " The light seems to seep into " + opponent.possessivePronoun() + " " + target.describe(opponent)
				+ ", leaving " + opponent.directObject() + " enthralled to " + self.possessivePronoun() + " will.";
				
			}
			c.write(self, message);
		}
		if (this.isType("pussy") && self.has(Trait.vaginaltongue) && target.isType("cock") && !opponent.hasStatus(Stsflag.cockbound)) {
			opponent.add(c, new CockBound(opponent, 5, self.name() + "'s pussy-tongue"));
			c.write(self, self.nameOrPossessivePronoun() + " long sinuous vaginal tongue wraps around "
						+ opponent.nameOrPossessivePronoun() + " " + target.describe(opponent) + ", preventing any escape.\n");
		}
		if (self.has(Trait.tight) || self.has(Trait.holecontrol)) {
			String desc = "";
			if (self.has(Trait.tight)) {desc += "powerful ";}
			if (self.has(Trait.holecontrol)) {desc += "well-traied ";}
			c.write(self, Global.format("{self:SUBJECT-ACTION:use|uses} {self:possessive} "+ desc +"vaginal muscles to milk {other:name-possessive} cock, adding to the pleasure.", self, opponent));
			bonus += (self.has(Trait.tight) && self.has(Trait.holecontrol)) ? 10 : 5;
		}
		return bonus;
	}
	@Override
	public String getFluids(Character c) {
		return "juices";
	}

	@Override
	public boolean isErogenous() {
		return true;
	}

	@Override
	public boolean isNotable() {
		return true;
	}
	@Override
	public BodyPart upgrade() {
		return this;
	}
	@Override
	public BodyPart downgrade() {
		return this;
	}

	@Override
	public String prefix() {
		if (desc.length() > 0)
			return "aieou".indexOf(desc.charAt(0)) >= 0 ? "an " : "a ";
		else 
			return "a";
	}

	@Override
	public int compare(BodyPart other) {
		return 0;
	}
	@Override
	public boolean isVisible(Character c) {
		return c.pantsless();
	}
	@Override
	public double applySubBonuses(Character self, Character opponent,
			BodyPart with, BodyPart target, double damage, Combat c) {
		return 0;
	}
	@Override
	public int mod(Attribute a, int total) {
		switch (a) {
		case Seduction:
			return (int) Math.round(this.hotness * 2);
		default:
			return 0;
		}
	}
	
	@Override
	public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
		if (self.has(Trait.autonomousPussy)) {
			c.write(self, Global.format("{self:NAME-POSSESSIVE} " + fullDescribe(self) + " churns against {other:name-possessive} cock, " +
					"seemingly with a mind of its own. Warm waves of flesh rubs against {other:possessive} shaft, elliciting groans of pleasure from {other:direct-object}.", self, opponent));
			opponent.body.pleasure(self, this, otherOrgan, 10, c);
		}
	}
}