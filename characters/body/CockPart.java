package characters.body;

import global.Global;

import java.io.PrintWriter;
import java.util.Scanner;

import combat.Combat;

import characters.Character;
import characters.Trait;

public enum CockPart implements BodyPart {
	tiny("tiny", 3),
	small("smallish", 4),
	average("average", 5),
	big("big", 6),
	huge("huge", 7),
	massive("massive", 8);

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
		if (c.pantsless()) {
			b.append("A ");
			b.append(describe(c));
			b.append(" hangs between " + c.nameOrPossessivePronoun() + " legs.");	
		}
	}

	@Override
	public String describe(Character c) {
		String syn = Global.pickRandom(synonyms);
		return Global.maybeString(desc + " ") + (c.hasPussy() ? "girl-" : "") + syn;
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
		double hotness = Math.log(size/8 + 1) / Math.log(2) / 4;
		if (!self.pantsless()) {
			hotness = 0;
		}
		if (!opponent.hasPussy()) {
			hotness /= 2;
		}
		return 1 + hotness;
	}

	@Override
	public double getPleasure(BodyPart target) {
		if (target.isType("pussy")) {
			double capacity = ((PussyPart)(target)).capacity;
			double effectiveSize = Math.max(0, size + Math.min(0, capacity - size));
			return Math.log(effectiveSize + 3) / Math.log(2) / 2;
		} else {
			return Math.log(size + 3) / Math.log(2) / 3;
		}
	}

	@Override
	public double getSensitivity(BodyPart target) {
		return Math.log(size/4 + 1) / Math.log(2);
	}

	public boolean isReady(Character self) {
		return self.has(Trait.alwaysready) || self.getArousal().percent() >= 15;
	}

	public static CockPart upgrade(CockPart b) {
		CockPart values[] = CockPart.values();
		if (b.ordinal() < values.length - 1) 
			return values[b.ordinal() + 1];
		else
			return null;
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

	public static BodyPart downgrade(CockPart target) {
		if (target.ordinal() > 0)
			return CockPart.values()[target.ordinal() - 1];
		else
			return null;
	}

	@Override
	public void save(PrintWriter saver) {
		saver.write(this.name());
	}

	public static BodyPart load(Scanner loader) {
		return CockPart.valueOf(loader.nextLine());
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		
		return damage;
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
		return damage;
	}
}