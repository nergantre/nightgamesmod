package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Hypersensitive extends DurationStatus {
	public Hypersensitive(Character affected) {
		super("Hypersensitive", affected, 20);
		flag(Stsflag.hypersensitive);
		flag(Stsflag.purgable);
	}

	@Override
	public String describe(Combat c) {
		if(affected.human()){
			return "Your skin tingles and feels extremely sensitive to touch.";
		}
		else{
			return "She shivers from the breeze hitting her skin and has goosebumps";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now hypersensitive.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public float fitnessModifier () {
		return -1;
	}

	@Override
	public int mod(Attribute a) {
		if(a == Attribute.Perception){
			return 4;
		}
		return 0;
	}
	
	@Override
	public int regen(Combat c) {
		super.regen(c);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
		return x/3;
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
		return 0;
	}
	public boolean lingering(){
		return true;
	}

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Hypersensitive(newAffected);
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new Hypersensitive(null);
	}
}
