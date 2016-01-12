package nightgames.characters.body;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;

public enum BreastsPart implements BodyPart {
	flat("flat", "", 0),
	a("A Cup", "tiny", 1),
	b("B Cup", "smallish", 2),
	c("C Cup", "modest", 3),
	d("D Cup", "round", 4),
	dd("DD Cup", "large", 5),
	e("E Cup", "huge", 6),
	f("F Cup", "glorious", 7);

	public String	desc;
	public String	name;
	public int		size;

	BreastsPart(String name, String desc, int size) {
		this.desc = desc;
		this.name = name;
		this.size = size;
	}

	public static String synonyms[] = { "breasts", "tits", "boobs", };

	@Override
	public void describeLong(StringBuilder b, Character c) {
		if (c.hasPussy() || size > 0) {
			b.append(Global.capitalizeFirstLetter(describe(c, true)));
			b.append(" adorn " + c.nameOrPossessivePronoun() + " chest.");
		}
	}

	@Override
	public double priority(Character c) {
		return getPleasure(c, null);
	}

	public String describe(Character c, boolean forceAdjective) {
		if (c.hasPussy() || size > 0) {
			if (forceAdjective) {
				boolean first = Global.random(2) == 0;
				boolean second = first ? Global.random(2) == 0 : true;
				return (first ? desc + ' ' : "") + (second ? name + ' ' : "")
						+ synonyms[Global.random(synonyms.length)];
			} else {
				return Global.maybeString(desc + ' ')
						+ Global.maybeString(name + ' ')
						+ synonyms[Global.random(synonyms.length)];
			}
		} else {
			if (c.get(Attribute.Power) > 25) {
				return "muscular pecs";
			}
			return "flat chest";
		}
	}

	@Override
	public String describe(Character c) {
		return describe(c, true);
	}

	@Override
	public String fullDescribe(Character c) {
		return describe(c, true);
	}

	@Override
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
		double hotness = -.25
				+ size * .3 * self.getOutfit().getExposure(ClothingSlot.top);
		if (!opponent.hasDick()) {
			hotness /= 2;
		}
		return Math.max(0, hotness);
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		return .25 + size * .35;
	}

	@Override
	public double getSensitivity(BodyPart target) {
		return .75 + size * .2;
	}

	@Override
	public BodyPart upgrade() {
		BreastsPart values[] = BreastsPart.values();
		if (ordinal() < values.length - 1) {
			return values[ordinal() + 1];
		} else {
			return this;
		}
	}

	@Override
	public double getFemininity(Character self) {
		return 3 * ((double)size) / maximumSize().size;
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

	@Override
	public BodyPart downgrade() {
		if (ordinal() > 0) {
			return BreastsPart.values()[ordinal() - 1];
		} else {
			return this;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject save() {
		JSONObject obj = new JSONObject();
		obj.put("enum", name());
		return obj;
	}

	@Override
	public BodyPart load(JSONObject obj) {
		return BreastsPart.valueOf((String) obj.get("enum"));
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		return Math.max(5, size) + Global.random(Math.min(0, size - 4));
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
		return 0;
	}

	@Override
	public String prefix() {
		return "";
	}

	@Override
	public int compare(BodyPart other) {
		if (other instanceof BreastsPart) {
			return size - ((BreastsPart) other).size;
		}
		return 0;
	}

	@Override
	public boolean isVisible(Character c) {
		return true;
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
				return -Math.max(size - 3, 0);
			case Seduction:
				return Math.max(size - 3, 0);
			default:
				return 0;
		}
	}

	@Override
	public void tickHolding(Combat c, Character self, Character opponent,
			BodyPart otherOrgan) {

	}

	@Override
	public int counterValue(BodyPart other) {
		return 0;
	}

	@Override
	public BodyPartMod getMod() {
		return BodyPartMod.noMod;
	}
}
