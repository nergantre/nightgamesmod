package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class IceStance extends DurationStatus {
	public IceStance(Character affected) {
		super("Ice Form", affected, 10);
		flag(Stsflag.form);
	}

	@Override
	public String describe(Combat c) {
		if (affected.human()) {
			return "You're as frigid as a glacier";
		} else {
			return affected.name() + " is cool as ice.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now in a ice stance.\n",
				affected.subjectAction("are", "is"));
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
	public int regen(Combat c) {
		super.regen(c);
		affected.emote(Emotion.confident, 5);
		affected.emote(Emotion.dominant, 5);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
		return -x/2;
	}

	@Override
	public int weakened(int x) {
		return 0;
	}

	@Override
	public int tempted(int x) {
		return -x / 2;
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
		return -x * 3 / 4;
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
		return new IceStance(newAffected);
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
		return new IceStance(null);
	}
}
