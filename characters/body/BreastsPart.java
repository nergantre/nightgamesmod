package characters.body;

import java.io.PrintWriter;
import java.util.Scanner;

import combat.Combat;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

public enum BreastsPart implements BodyPart {
	flat("flat", "", 0),
	a("A Cup", "tiny", 1),
	b("B Cup", "smallish", 2),
	c("C Cup", "perky", 3),
	d("D Cup", "round", 4),
	dd("DD Cup", "large", 5),
	e("E Cup", "huge", 6),
	f("F Cup", "glorious", 7);

	public String desc;
	public String name;
	public int size;
	BreastsPart(String name, String desc, int size) {
		this.desc = desc;
		this.name = name;
		this.size = size;
	}
	public static String synonyms[] = {
		"breasts", "tits", "boobs",
	};
	@Override
	public void describeLong(StringBuilder b, Character c) {
		if (c.hasPussy() || size > 0) {
			b.append(Global.capitalizeFirstLetter(describe(c,true)));
			b.append(" adorn " + c.nameOrPossessivePronoun() + " chest.");
		}
	}

	public String describe(Character c, boolean forceAdjective) {
		if (c.hasPussy() || size > 0) {
			if (forceAdjective) {
				boolean first = Global.random(2) == 0;
				boolean second = first ? Global.random(2) == 0 : true;
				return (first ? desc + ' ' : "") + (second ? name + ' ' : "") + synonyms[Global.random(synonyms.length)];
			} else {
				return Global.maybeString(desc + ' ') + Global.maybeString(name + ' ') + synonyms[Global.random(synonyms.length)];
			}
		} else {
			if (c.getPure(Attribute.Power) > 15) {
				return "muscular pecks";
			}
			return "flat chest";
		}
	}
	
	@Override
	public String describe(Character c) {
		return describe(c, true);
	}

	public boolean isType(String type) {
		return type.equalsIgnoreCase("breasts");
	}

	@Override
	public String getType() {
		return "breasts";
	}

	@Override
	public String toString() {
		return desc + ' ' + name;
	}
	
	@Override
	public boolean isReady(Character self) {
		return true;
	}	

	@Override
	public double getHotness(Character self, Character opponent) {
		double hotness = Math.log(size + 1) / Math.log(2) / 4;
		if (!self.topless())
			hotness /= 2;
		if (!opponent.hasDick()) {
			hotness /= 2;
		}
		return 1.0 + hotness;
	}

	@Override
	public double getPleasure(BodyPart target) {
		return Math.log(size + 3) / Math.log(2) / 2;
	}

	@Override
	public double getSensitivity(BodyPart target) {
		return Math.log(size + 2) / Math.log(2) / 2;
	}

	public static BreastsPart upgrade(BreastsPart b) {
		BreastsPart values[] = BreastsPart.values();
		if (b.ordinal() < values.length - 1) 
			return values[b.ordinal() + 1];
		else
			return null;
	}

	public static BreastsPart maximumSize() {
		BreastsPart max = flat;
		for (BreastsPart b : BreastsPart.values()) {
			if (b.size > max.size) {
				max = b;
			}
		}
		return max;
	}

	public static BodyPart downgrade(BreastsPart target) {
		if (target.ordinal() > 0)
			return BreastsPart.values()[target.ordinal() - 1];
		else
			return null;
	}

	@Override
	public void save(PrintWriter saver) {
		saver.write(this.name());
	}

	public static BodyPart load(Scanner loader) {
		return BreastsPart.valueOf(loader.nextLine());
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		return damage;
	}

	@Override
	public String getFluids(Character c) {
		return c.has(Trait.lactating) ? "milk" : "";
	}

	@Override
	public boolean isErogenous() {
		return size > BreastsPart.flat.size;
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