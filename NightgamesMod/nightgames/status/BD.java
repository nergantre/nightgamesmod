package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class BD extends DurationStatus {
	public BD(Character affected) {
		super("Bondage", affected, 10);
		flag(Stsflag.bondage);
		flag(Stsflag.purgable);
	}

	@Override
	public String describe(Combat c) {
		if(affected.human()){
			return "Fantasies of being tied up continue to dance through your head.";
		}
		else{
			return affected.name()+" is affected by a brief bondage fetish.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now affected by a bondage fetish.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public float fitnessModifier () {
		return -1;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public void tick(Combat c) {
		if(affected.bound()){
			affected.arouse(affected.getArousal().max()/20, c);
		}
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
		return affected.is(Stsflag.bound) ? x / 2 : 0;
	}

	@Override
	public int weakened(int x) {
		return 0;
	}

	@Override
	public int tempted(int x) {
		return affected.is(Stsflag.bound) ? x / 2 : 0;
	}

	@Override
	public int evade() {
		return 0;
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
		return 0;
	}

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new BD(newAffected);
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new BD(null);
	}
}
