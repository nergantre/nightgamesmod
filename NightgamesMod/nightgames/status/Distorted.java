package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class Distorted extends DurationStatus {
	public Distorted(Character affected, int duration) {
		super("Distorted", affected, duration);
		flag(Stsflag.distorted);
		flag(Stsflag.purgable);
	}

	@Override
	public String describe(Combat c) {
		if (affected.human()) {
			return "Your image is distorted, making you hard to hit.";
		} else {
			return "Multiple " + affected.name()
					+ "s appear in front of you. When you focus, you can tell which one is real, but it's still screwing up your accuracy.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s image is now distorted.\n",
				affected.nameOrPossessivePronoun());
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
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
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
		return new Distorted(newAffected, getDuration());
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
		return new Distorted(null, JSONUtils.readInteger(obj, "duration"));
	}
}
