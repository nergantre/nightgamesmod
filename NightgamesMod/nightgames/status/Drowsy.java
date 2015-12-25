package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class Drowsy extends DurationStatus {

	private int magnitude;

	public Drowsy(Character affected) {
		super("Drowsy", affected, affected.has(Trait.PersonalInertia) ? 4 : 6);
		flag(Stsflag.drowsy);
		magnitude = 1;
	}

	public Drowsy(Character affected, int magnitude, int duration) {
		super("Drowsy", affected, duration);
		this.magnitude = magnitude;
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return "Your head feels woozy, as if you've just woken up.";
	}

	@Override
	public String describe(Combat c) {
		if (affected.human()) {
			return "You feel lethargic and sluggish. You're struggling to remain standing";
		}

		return affected.name() + " looks extremely sleepy.";
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		super.regen(c);
		return -3 * magnitude;
	}

	@Override
	public void tick(Combat c) {
		affected.loseMojo(c, magnitude * -5);
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, double x) {
		return -(x * magnitude) / 4;
	}

	@Override
	public int weakened(int x) {
		return x * magnitude / 4;
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
		return x * magnitude / 3;
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
		return new Drowsy(newAffected);
	}

	@Override
	public boolean lingering() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("magnitude", magnitude);
		obj.put("duration", getDuration());
		return obj;
	}

	@Override
	public Status loadFromJSON(JSONObject obj) {
		return new Drowsy(null, JSONUtils.readInteger(obj, "magnitude"),
				JSONUtils.readInteger(obj, "duration"));
	}

}
