package characters.body;

import global.Global;
import status.Horny;

import java.io.PrintWriter;
import java.util.Scanner;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Trait;

public enum CockPart implements BodyPart {
	tiny("tiny", 3),
	small("smallish", 4),
	average("average", 6),
	big("big", 8),
	huge("huge", 9),
	massive("massive", 10);

	public String desc;
	public double size;
	public static String synonyms[] = {
		"cock", "dick", "shaft", "phallus"
	};
	
	CockPart(String desc, double size) {
		this.desc = desc;
		this.size = size;
	}

	@Override
	public void describeLong(StringBuilder b, Character c) {
		b.append("A ");
		b.append(fullDescribe(c));
		b.append(" hangs between " + c.nameOrPossessivePronoun() + " legs.");
	}

	@Override
	public String describe(Character c) {
		String syn = Global.pickRandom(synonyms);
		return Global.maybeString(desc + " ") + (c.hasPussy() ? "girl-" : "") + syn;
	}

	@Override
	public double priority(Character c) {
		return this.getPleasure(c, null);
	}
	
	@Override
	public String fullDescribe(Character c) {
		String syn = Global.pickRandom(synonyms);
		return desc + " " + (c.hasPussy() ? "girl-" : "") + syn;
	}

	public boolean isType(String type) {
		return type.equalsIgnoreCase("cock");
	}

	@Override
	public String getType() {
		return "cock";
	}

	@Override
	public String toString() {
		return desc;
	}

	@Override
	public double getHotness(Character self, Character opponent) {
		double hotness = Math.log(size/4 + 1) / Math.log(2) - 1;
		if (!opponent.hasPussy()) {
			hotness /= 2;
		}
		return hotness;
	}

	public double getPleasureBase() {
		return Math.log(size + 2.5) / Math.log(2) - 1.8;
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		double pleasureMod = getPleasureBase();
		pleasureMod += self.has(Trait.cockTraining1) ? .5 : 0;
		pleasureMod += self.has(Trait.cockTraining2) ? .7 : 0;
		pleasureMod += self.has(Trait.cockTraining3) ? .7 : 0;
		return pleasureMod;
	}

	@Override
	public double getSensitivity(BodyPart target) {
		return Math.log(size/5 + 1) / Math.log(2);
	}

	public boolean isReady(Character self) {
		return self.has(Trait.alwaysready) || self.getArousal().percent() >= 15;
	}

	public BodyPart upgrade() {
		CockPart values[] = CockPart.values();
		if (ordinal() < values.length - 1) 
			return values[ordinal() + 1];
		else
			return this;
	}

	public static CockPart maximumSize() {
		CockPart max = tiny;
		for (CockPart c : CockPart.values()) {
			if (c.size > max.size) {
				max = c;
			}
		}
		return max;
	}

	public BodyPart downgrade() {
		if (ordinal() > 0)
			return CockPart.values()[ordinal() - 1];
		else
			return this;
	}

	@Override
	public void save(PrintWriter saver) {
		saver.write(this.name());
	}

	@Override
	public BodyPart load(Scanner loader) {
		return CockPart.valueOf(loader.nextLine());
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (self.has(Trait.polecontrol)) {
			String desc = "";
			if (self.has(Trait.polecontrol)) {desc += "expert ";}
			c.write(self, Global.format("{self:SUBJECT-ACTION:use|uses} {self:possessive} "+ desc +"cock control to grind against {other:name-possessive} inner walls, making {other:possessive} knuckles whiten as {other:subject-action:moan|moans} uncontrollably.", self, opponent));
			bonus += (self.has(Trait.polecontrol)) ? 8 : 0;
		}
		return bonus;
	}

	@Override
	public String getFluids(Character c) {
		return "pre-cum";
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
	public double applyReceiveBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (opponent.has(Trait.dickhandler)) {
			c.write(opponent, Global.format("{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} cock causes {self:subject} to shudder uncontrollably.",
					self, opponent));
			bonus += 5;
		}
		return bonus;
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
		if (other instanceof CockPart) {
			return (int) (size - ((CockPart)other).size);
		}
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
		case Speed:
			return (int) -Math.round(Math.max(size - 6, 0));
		case Seduction:
			return (int) Math.round(Math.max(size - 6, 0));
		default:
			return 0;
		}
	}

	@Override
	public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {

	}
}