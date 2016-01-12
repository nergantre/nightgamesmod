package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public class Cynical extends DurationStatus {
	public Cynical(Character affected) {
		super("Cynical", affected, 3);
		flag(Stsflag.cynical);
	}

	@Override
	public String describe(Combat c) {
		if (affected.human()) {
			return "You're feeling more cynical than usual and won't fall for any mind games.";
		} else {
			return affected.name() + " has a cynical edge in her eyes.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now cynical towards future mind games.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public float fitnessModifier() {
		return 1;
	}

	@Override
	public int mod(Attribute a) {
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
		return -5;
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
		return -x;
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
		return new Cynical(newAffected);
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
		return new Cynical(null);
	}
}
