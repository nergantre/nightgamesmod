package nightgames.characters.body;

import java.util.Arrays;
import java.util.Optional;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;

public enum BasicCockPart implements CockPart {
	tiny("tiny", 3), small("smallish", 4), average("average-sized", 6), big("big", 8), huge("huge",
			9), massive("massive", 10);

	public String desc;
	public double size;
	public static String synonyms[] = { "cock", "dick", "shaft", "phallus" };

	BasicCockPart(String desc, double size) {
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
		return getPleasure(c, null);
	}

	@Override
	public String fullDescribe(Character c) {
		String syn = Global.pickRandom(synonyms);
		return desc + " " + (c.hasPussy() ? "girl-" : "") + syn;
	}

	@Override
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
		double hotness = Math.log(size / 4 + 1) / Math.log(2) - 1;
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
		return Math.log(size / 5 + 1) / Math.log(2);
	}

	@Override
	public boolean isReady(Character self) {
		return self.has(Trait.alwaysready) || self.getArousal().percent() >= 15;
	}

	@Override
	public BodyPart upgrade() {
		BasicCockPart values[] = BasicCockPart.values();
		if (ordinal() < massive.ordinal()) {
			return values[ordinal() + 1];
		} else {
			return this;
		}
	}

	public static BasicCockPart maximumSize() {
		BasicCockPart max = tiny;
		for (BasicCockPart c : BasicCockPart.values()) {
			if (c.size > max.size) {
				max = c;
			}
		}
		return max;
	}

	@Override
	public BodyPart downgrade() {
		if (ordinal() > 0 && ordinal() <= massive.ordinal()) {
			return BasicCockPart.values()[ordinal() - 1];
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
		String enumName = (String) obj.get("enum");
		// some compatibility for old versions
		Optional<CockMod> mod = Arrays.stream(CockMod.values()).filter(modVal -> modVal.name().equals(enumName))
				.findAny();
		if (mod.isPresent()) {
			return new ModdedCockPart(big, mod.get());
		}
		return BasicCockPart.valueOf(enumName);
	}

	@Override
	public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (self.has(Trait.polecontrol)) {
			String desc = "";
			if (self.has(Trait.polecontrol)) {
				desc += "expert ";
			}
			c.write(self,
					Global.format(
							"{self:SUBJECT-ACTION:use|uses} {self:possessive} " + desc
									+ "cock control to grind against {other:name-possessive} inner walls, making {other:possessive} knuckles whiten as {other:pronoun} {other:action:moan|moans} uncontrollably.",
							self, opponent));
			bonus += self.has(Trait.polecontrol) ? 8 : 0;
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
	public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (opponent.has(Trait.dickhandler)) {
			c.write(opponent,
					Global.format(
							"{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} cock causes {self:subject} to shudder uncontrollably.",
							self, opponent));
			bonus += 5;
		}
		return bonus;
	}

	@Override
	public String prefix() {
		return "a ";
	}

	@Override
	public int compare(BodyPart other) {
		if (other instanceof BasicCockPart) {
			return (int) (size - ((BasicCockPart) other).size);
		}
		return 0;
	}

	@Override
	public boolean isVisible(Character c) {
		return c.crotchAvailable();
	}

	@Override
	public double applySubBonuses(Character self, Character opponent, BodyPart with, BodyPart target, double damage,
			Combat c) {
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

	@Override
	public int counterValue(BodyPart other) {
		// Don't fuck modded parts, they're dangerous
		return other.isGeneric() ? 0 : -1;
	}

	@Override
	public double getSize() {
		return size;
	}

	@Override
	public BodyPartMod getMod() {
		return BodyPartMod.noMod;
	}

	@Override
	public BodyPart applyMod(CockMod mod) {
		return new ModdedCockPart(this, mod);
	}
}