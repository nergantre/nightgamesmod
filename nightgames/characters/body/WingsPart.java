package nightgames.characters.body;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public enum WingsPart implements BodyPart {
	demonic("demonic ", .2, 1.3, 1.2),
	normal("", 0, 1, 1);
	public String desc;
	public double hotness;
	public double pleasure;
	public double sensitivity;
	WingsPart(String desc, double hotness, double pleasure, double sensitivity) {
		this.desc = desc;
		this.hotness = hotness;
		this.sensitivity = sensitivity;
		this.pleasure = pleasure;
	}

	@Override
	public void describeLong(StringBuilder b, Character c) {
		b.append("A pair of " + describe(c) + " sits gracefully between " + c.nameOrPossessivePronoun() + " shoulder blades.");
	}

	@Override
	public String describe(Character c) {
		return desc + "wings";
	}

	@Override
	public String fullDescribe(Character c) {
		return desc + "wings";
	}
	
	@Override
	public String toString() {
		return desc + "wings";
	}
	
	public boolean isType(String type) {
		return type.equalsIgnoreCase("wings");
	}

	@Override
	public String getType() {
		return "wings";
	}

	@Override
	public double getHotness(Character self, Character opponent) {
		return hotness;
	}

	@Override
	public double priority(Character c) {
		return this.getPleasure(c, null);
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		return pleasure;
	}

	@Override
	public double getSensitivity(BodyPart target) {
		return sensitivity;
	}
	@Override
	public boolean isReady(Character self) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject save() {
		JSONObject obj = new JSONObject();
		obj.put("enum", this.name());
		return obj;
	}

	@Override
	public BodyPart load(JSONObject obj) {
		return WingsPart.valueOf((String)obj.get("enum"));
	}


	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		return 0;
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
		return true;
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
		return "";
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
		switch (a) {
		case Speed:
			return 2;
		default:
			return 0;
		}
	}
	
	@Override
	public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {

	}
}
