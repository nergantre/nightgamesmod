package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public class Braced extends DurationStatus {

	public Braced(Character affected) {
		super("Braced", affected, 3);
		flag(Stsflag.braced);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now braced.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public String describe(Combat c) {
		return "";
	}

	@Override
	public float fitnessModifier() {
		return (10.0f + 10.0f * getDuration()) / 40.f;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		return -x * 3 / 4;
	}

	@Override
	public double pleasure(Combat c, double x) {
		return 0;
	}

	@Override
	public int weakened(int x) {
		return -x * 3 / 4;
	}

	@Override
	public int drained(int x) {
		return -x * 3 / 4;
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
		return 30 + 30 * getDuration();
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Braced(newAffected);
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		return obj;
	}

	@Override
	public Status loadFromJSON(JSONObject obj) {
		return new Braced(null);
	}
}
