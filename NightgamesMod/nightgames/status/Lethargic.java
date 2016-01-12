package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class Lethargic extends DurationStatus {
	double magnitude;

	public Lethargic(Character affected, int duration, double magnitude) {
		super("Lethargic", affected, duration);
		this.magnitude = magnitude;
		flag(Stsflag.lethargic);
	}

	@Override
	public boolean lingering() {
		return true;
	}

	@Override
	public String describe(Combat c) {
		if (affected.human()) {
			return "Your mojo gain is stopped.";
		} else if (affected.has(Trait.lethargic)) {
			if (affected.getMojo().get() < 40) {
				return affected.name() + " looks lethargic.";
			} else {
				return affected.name() + " looks energized";
			}
		} else {
			return affected.name() + " looks lethargic.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s lethargic.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public float fitnessModifier() {
		return -3f;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
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
		return 10;
	}

	@Override
	public int escape() {
		return 0;
	}

	@Override
	public int gainmojo(int x) {
		return (int) Math.round(-x * magnitude);
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
		return new Lethargic(newAffected, getDuration(), magnitude);
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("duration", getDuration());
		obj.put("magnitude", magnitude);
		return obj;
	}

	@Override
	public Status loadFromJSON(JSONObject obj) {
		return new Lethargic(null, JSONUtils.readInteger(obj, "duration"), JSONUtils.readFloat(obj, "magnitude"));
	}
}
