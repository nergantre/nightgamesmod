package nightgames.status;

import java.util.Arrays;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.characters.custom.requirement.InsertedRequirement;
import nightgames.characters.custom.requirement.ReverseRequirement;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class Knotted extends Status {

	private Character	opponent;
	private boolean		anal;

	public Knotted(Character affected, Character other, boolean anal) {
		super("Knotted", affected);
		opponent = other;
		this.anal = anal;
		requirements.add(new ReverseRequirement(
				Arrays.asList(new InsertedRequirement(true))));
		flag(Stsflag.knotted);
		flag(Stsflag.purgable);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String
				.format("The base of %s %s swells up, forming a tight seal within %s %s and keeping it inside.",
						opponent.nameOrPossessivePronoun(),
						c.getStance().insertedPartFor(opponent).describe(
								opponent),
						affected.nameOrPossessivePronoun(),
						c.getStance().insertablePartFor(affected)
								.describe(affected));
	}

	@Override
	public String describe(Combat c) {
		if (affected.human()) {
			return opponent.nameOrPossessivePronoun()
					+ " knotted dick is lodged inside of you, preventing escape.";
		} else {
			return "The knot in you dick is keeping it fully entrenched within "
					+ affected.name() + ".";
		}
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		affected.emote(Emotion.desperate, 10);
		affected.emote(Emotion.nervous, 10);
		affected.emote(Emotion.horny, 20);
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
		return -15;
	}

	@Override
	public int escape() {
		return -15;
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
		return -10;
	}

	@Override
	public int value() {
		return 0;
	}

	@Override
	public float fitnessModifier() {
		// This is counted twice, but that's intentional.
		// (The other place is Character#getFitness())
		return affected.body.penetrationFitnessModifier(false, anal,
				opponent.body);
	}

	@Override
	public String toString() {
		return "Knotted dick locked inside";
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Knotted(newAffected, newOther, anal);
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("anal", anal);
		return obj;
	}

	@Override
	public Status loadFromJSON(JSONObject obj) {
		return new Knotted(null, null, JSONUtils.readBoolean(obj, "anal"));
	}

}
