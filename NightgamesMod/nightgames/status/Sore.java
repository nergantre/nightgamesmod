package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class Sore extends DurationStatus {
	public Sore(Character affected, int duration) {
		super("Sore", affected, duration);

		flag(Stsflag.purgable);
		flag(Stsflag.sore);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now sore.\n",
				affected.subjectAction("are", "is"));
	}

	@Override
	public String describe(Combat c) {
		return "";
	}

	@Override
	public float fitnessModifier() {
		return -1f;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		super.regen(c);
		affected.emote(Emotion.nervous,10);
		return -affected.getStamina().max()/20;
	}

	@Override
	public int damage(Combat c, int x) {
		return 5;
	}

	@Override
	public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
		return 0;
	}

	@Override
	public int weakened(int x) {
		return x;
	}

	@Override
	public int tempted(int x) {
		return 0;
	}

	@Override
	public int evade() {
		return -10;
	}

	@Override
	public int escape() {
		return -10;
	}

	@Override
	public int gainmojo(int x) {
		return -x/2;
	}

	@Override
	public int spendmojo(int x) {
		return x * 2;
	}

	@Override
	public int counter() {
		return 0;
	}

	@Override
	public boolean lingering() {
		return true;
	}

	@Override
	public int value() {
		return 0;
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Sore(newAffected, getDuration());
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("duration", getDuration());
		return obj;
	}

	@Override
	public Status loadFromJSON(JSONObject obj) {
		return new Sore(null, JSONUtils.readInteger(obj, "duration"));
	}

}
