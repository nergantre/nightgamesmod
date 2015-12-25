package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public class Unreadable extends DurationStatus {
	public Unreadable(Character affected) {
		super("Unreadable", affected, 3);
		flag(Stsflag.unreadable);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now unreadable.\n",
				affected.subjectAction("are", "is"));
	}

	@Override
	public String describe(Combat c) {
		return null;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public float fitnessModifier() {
		return 1f;
	}

	@Override
	public int regen(Combat c) {
		super.regen(c);
		return 0;
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
		return new Unreadable(newAffected);
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
		return new Unreadable(null);
	}
}
