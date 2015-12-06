package nightgames.characters.body;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;

import org.json.simple.JSONObject;

public class GenericBodyPart implements BodyPart {
	/**
	 * 
	 */
	public String type;
	public String desc;
	public String prefix;
	public double hotness;
	public double sensitivity;
	public double pleasure;
	public String descLong;
	private boolean notable;

	public GenericBodyPart(String desc, String descLong, double hotness, double pleasure, double sensitivity, boolean notable, String type, String prefix) {
		this.desc = desc;
		this.descLong = descLong;
		this.hotness = hotness;
		this.pleasure = pleasure;
		this.sensitivity = sensitivity;
		this.type = type;
		this.notable = notable;
		this.prefix = prefix;
	}

	public GenericBodyPart(String desc, double hotness, double pleasure, double sensitivity, String type, String prefix) {
		this(desc, "", hotness, pleasure, sensitivity, false, type, prefix);
	}

	public GenericBodyPart(String desc, double hotness, double pleasure, double sensitivity, boolean notable, String type, String prefix) {
		this(desc, "", hotness, pleasure, sensitivity, notable, type, prefix);
	}

	@Override
	public void describeLong(StringBuilder b, Character c) {
		String parsedDesc = Global.format(descLong, c, c);
		b.append(parsedDesc);
	}

	@Override
	public boolean isType(String type) {
		return this.type.equalsIgnoreCase(type);
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String describe(Character c) {
		return desc;
	}

	@Override
	public double priority(Character c) {
		return (this.getPleasure(c, null) - 1) * 3;
	}

	@Override
	public String fullDescribe(Character c) {
		if (notable)
			return desc;
		else
			return "normal " +desc;
	}

	@Override
	public String toString() {
		return fullDescribe(null);
	}

	@Override
	public double getHotness(Character self, Character opponent) {
		return hotness;
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		double pleasureMod = pleasure;
		if (type.equals("hands") || type.equals("feet")) {
			pleasureMod += self.has(Trait.limbTraining1) ? .5 : 0;
			pleasureMod += self.has(Trait.limbTraining2) ? .7 : 0;
			pleasureMod += self.has(Trait.limbTraining3) ? .7 : 0;
			pleasureMod += self.has(Trait.dexterous) ? .4 : 0;
		}
		if (type.equals("hands")) {
			pleasureMod += self.has(Trait.pimphand) ? .5 : 0;
		}
		return pleasureMod;
	}

	@Override
	public double getSensitivity(BodyPart target) {
		return sensitivity;
	}

	@Override
	public boolean isReady(Character self) {
		return true;
	}

	@Override
	public boolean equals(Object other)
	{
		return this.toString().equals(other.toString());
	}

	@Override
	public int hashCode() {
		return (this.type + ":" +this.toString()).hashCode();
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToDict() {
		JSONObject res = new JSONObject();
		res.put("desc",			desc);
		res.put("descLong",		descLong);
		res.put("hotness",		hotness);
		res.put("pleasure",		pleasure);
		res.put("sensitivity",	sensitivity);
		res.put("notable",		notable);
		res.put("type",			type);
		res.put("prefix",		prefix);

		return res;
	}

	@SuppressWarnings("unchecked")
	public BodyPart loadFromDict(JSONObject dict) {
		try {
			// newly added field
			if (!dict.containsKey("generic")) {
				dict.put("generic", true);
			}
			GenericBodyPart part = new GenericBodyPart(
										(String)dict.get("desc"),
										(String)dict.get("descLong"),
										((Number)dict.get("hotness")).doubleValue(),
										((Number)dict.get("pleasure")).doubleValue(),
										((Number)dict.get("sensitivity")).doubleValue(),
										(Boolean)dict.get("notable"),
										(String)dict.get("type"),
										(String)dict.get("prefix"));
			return part;
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	@Override
	public JSONObject save() {
		return saveToDict();
	}

	public BodyPart load(JSONObject obj) {
		return loadFromDict(obj);
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		int bonus = 0;
		if (self.has(ClothingTrait.nursegloves) && type.equals("hands")) {
			c.write(self, Global.format("{self:name-possessive} rubber gloves provide an unique sensation as {self:subject-action:run|runs} {self:possessive} hands over {other:possessive} " +target.describe(opponent), self, opponent));
			bonus += 5 + Global.random(5);
			if (Global.random(5) == 0) {
				c.write(self, "Unfortunately, the gloves wear out with their usage.");
				self.shred(ClothingSlot.hands);
			}
		}
		return bonus;
	}

	@Override
	public String getFluids(Character c) {
		return "";
	}

	@Override
	public boolean isErogenous() {
		return false;
	}

	@Override
	public boolean isNotable() {
		return notable;
	}
	@Override
	public double applyReceiveBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		return 0;
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
		return prefix;
	}
	
	@Override
	public int compare(BodyPart other) {
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
		return 0;
	}

	@Override
	public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {

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
