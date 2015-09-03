package nightgames.status;

import java.util.Arrays;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.custom.requirement.EitherInsertedRequirement;
import nightgames.characters.custom.requirement.InsertedRequirement;
import nightgames.characters.custom.requirement.OrRequirement;
import nightgames.characters.custom.requirement.ReverseRequirement;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class ArmLocked extends Status {
	private float toughness;
	
	public ArmLocked(Character affected, float dc) {
		super("Arm Locked", affected);
		toughness = dc;
		requirements.add(new EitherInsertedRequirement(true));
		requirements.add((c, self, other) -> toughness > .01);
		flag(Stsflag.armlocked);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Her hands are entwined with your own, preventing your escape.";
		}
		else{
			return "Your hands are entwined with hers, preventing her escape.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s being held down.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public float fitnessModifier () {
		return -toughness / 10.0f;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		affected.emote(Emotion.horny, 10);
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
		return -15;
	}

	@Override
	public int escape() {
		return -Math.round(toughness);
	}

	@Override
	public void struggle(Character self) {
		toughness = Math.round(toughness * .5f);
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

	public String toString(){
		return "Bound by hands";
	}

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new ArmLocked(newAffected, Math.round(toughness));
	}
	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("toughness", toughness);
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new ArmLocked(null, JSONUtils.readFloat(obj, "toughness"));
	}
}
