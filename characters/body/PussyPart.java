package characters.body;

import global.Global;

import java.io.PrintWriter;
import java.util.Scanner;

import combat.Combat;

import status.Abuff;
import status.CockBound;
import status.Enthralled;
import status.Horny;
import status.Shamed;
import status.Stsflag;

import characters.Attribute;
import characters.Character;
import characters.Trait;

public enum PussyPart implements BodyPart {
	normal("", 0, 1, 1, 6, 15, 0),
	arcane("arcane patterned ", .2, 1.1, 1, 9, 5, 3),
	fiery("fiery ", 0, 1.3, 1.2, 8, 15, 3),
	succubus("succubus ", 1, 1.5, 1.2, 999, 0, 4),
	feral("feral ", 1, 1.3, 1.2, 8, 7, 2),
	cybernetic("cybernetic ", -.50, 1.8, .5, 200, 0, 4);

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
		if (c.pantsless()) {
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
	}

	@Override
	public String describe(Character c) {
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
		if (self.pantsless())
			val = 0;
		if (!opponent.hasDick())
			val /= 2;
		return 1 + val;
	}

	@Override
	public double getPleasure(BodyPart target) {
		return pleasure;
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

	public static BodyPart load(Scanner loader) {
		return PussyPart.valueOf(loader.nextLine());
	}

	@Override
	public double applyReceiveBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		if (this==PussyPart.feral) {
			c.write(self, String.format("Musk emanating from %s %s leaves %s reeling.",
					self.possessivePronoun(), describe(self), opponent.directObject()));
			opponent.add(new Horny(opponent, (int) Math.max(1, Math.floor(damage/5)), 5, self.nameOrPossessivePronoun() + " feral musk"));
		}
		return damage;
	}
	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {

		if (this==PussyPart.succubus && target.isType("cock")) {
			c.write(self, String.format("%s hot flesh wraps around %s %s and starts squirming chaotically. "
										+"Suddenly, %s feel%s something that is not cum shoot out of %s %s, which is greedily absorbed by %s %s",
					self.possessivePronoun(), opponent.possessivePronoun(), target.describe(opponent),
					opponent.pronoun(), opponent.human() ? "" : "s",  opponent.possessivePronoun(), target.describe(opponent),
					self.possessivePronoun(), describe(self)));
			int strength = 10 + self.get(Attribute.Dark)/2;
			opponent.weaken(c, strength);
			self.heal(c, strength);
			opponent.spendMojo(strength);
			self.buildMojo(strength);
			for (int i = 0; i < 10; i++) {
				Attribute stolen = (Attribute) opponent.att.keySet().toArray()[Global.random(opponent.att.keySet().size())];
				if (stolen != Attribute.Perception && opponent.get(stolen) > 0) {
					int stolenStrength = Math.min(strength / 10, opponent.get(stolen));
					opponent.add(new Abuff(opponent, stolen, -stolenStrength, 20));
					self.add(new Abuff(self, stolen, stolenStrength, 20));
					break;
				}
			}
		}
		if (this==PussyPart.cybernetic && target.isType("cock") && Global.random(3) == 0) {
			c.write(self, String.format("%s %s whirls to life and starts attempting to extract all the semen packed inside %s %s. "
										+"At the same time, %s feel a thin filament sliding into %s urethra, filling %s with both pleasure and shame.",
					self.possessivePronoun(), describe(self), opponent.possessivePronoun(), target.describe(opponent),
					opponent.pronoun(), opponent.possessivePronoun(), opponent.directObject()));
			damage *= 2;
			opponent.add(new Shamed(opponent));
		}
		if (this==PussyPart.fiery && target.isType("cock")) {
			c.write(self, String.format("Pluging %s %s into %s %s leaves %s gasping from the heat.",
					opponent.possessivePronoun(), target.describe(opponent), self.possessivePronoun(), describe(self),
					opponent.directObject()));
			opponent.pain(c, 20 + self.get(Attribute.Ki)/2);
		}
		if (this==PussyPart.arcane && Global.random(4) == 0 && !opponent.wary()) {
			opponent.add(new Enthralled(opponent, self, 3));
			c.write(self, self.nameOrPossessivePronoun() + " tattoos surrounding " + self.possessivePronoun() + " vagina suddenly light up with arcane energy. " 
						+ "The light seems to seep into " + opponent.possessivePronoun() + " " + target.describe(opponent)
						+ ", leaving " + opponent.directObject() + " enthralled to " + self.possessivePronoun() + " will.");
		}
		if (this.isType("pussy") && self.has(Trait.vaginaltongue) && target.isType("cock") && !opponent.hasStatus(Stsflag.cockbound)) {
			opponent.add(new CockBound(opponent, 5, self.name() + "'s pussy-tongue"));
			c.write(self, self.nameOrPossessivePronoun() + " long sinuous vaginal tongue wraps around "
						+ opponent.nameOrPossessivePronoun() + " " + target.describe(opponent) + ", preventing any escape.\n");
		}
		return damage;
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
}