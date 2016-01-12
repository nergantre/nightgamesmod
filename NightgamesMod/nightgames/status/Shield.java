package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class Shield extends DurationStatus {
	private double strength;

	public Shield(Character affected, double strength) {
		this(affected, strength, 4);
	}

	public Shield(Character affected, double strength, int duration) {
		super("Shield", affected, duration);
		this.strength = strength;
		flag(Stsflag.shielded);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now shielded.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public String describe(Combat c) {
		return "";
	}

	@Override
	public float fitnessModifier() {
		return (float) strength * 4;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		super.regen(c);
		affected.emote(Emotion.confident, 5);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		return (int) -Math.round(x * strength);
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
		return 2;
	}

	@Override
	public int value() {
		return 0;
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Shield(newAffected, strength, getDuration());
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("strength", strength);
		obj.put("duration", getDuration());
		return obj;
	}

	@Override
	public Status loadFromJSON(JSONObject obj) {
		return new Shield(null, JSONUtils.readInteger(obj, "strength"), JSONUtils.readInteger(obj, "duration"));
	}
}
