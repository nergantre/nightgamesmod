package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.custom.requirement.InsertedRequirement;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class DivineCharge extends Status {
	public double magnitude;

	public DivineCharge(Character affected, double magnitude) {
		super("Divine Charge", affected);
		flag(Stsflag.divinecharge);
		this.magnitude = magnitude;
		requirements.add(new InsertedRequirement(true));
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		if (!replaced)
			return String.format("%s concentrating divine energy in %s pussy.\n", affected.subjectAction("are", "is"), affected.possessivePronoun());
		return "";
	}

	@Override
	public String describe() {
		return "Concentrated divine energy surges through " + affected.nameOrPossessivePronoun() + " pussy.";
	}

	@Override
	public float fitnessModifier () {
		return (float) (3 * magnitude);
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public boolean overrides(Status s) {
		return false;
	}

	@Override
	public void replace(Status s) {
		assert (s instanceof DivineCharge);
		DivineCharge other = (DivineCharge)s;
		this.magnitude = this.magnitude + other.magnitude;
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, double x) {
		return 0;
	}

	@Override
	public int weakened(int x) {
		return 0;
	}

	@Override
	public int tempted(int x) {
		return 0;
	}

	@Override
	public int evade() {
		return 0;
	}

	@Override
	public int escape() {
		return 0;
	}

	@Override
	public int gainmojo(int x) {
		return 0;
	}

	@Override
	public int spendmojo(int x) {
		return 0;
	}

	@Override
	public int counter() {
		return 0;
	}

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new DivineCharge(newAffected, magnitude);
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("magnitude", magnitude);
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new DivineCharge(null, JSONUtils.readFloat(obj, "magnitude"));
	}

	@Override
	public int regen(Combat c) {
		return 0;
	}
}
