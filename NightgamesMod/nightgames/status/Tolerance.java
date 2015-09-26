package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class Tolerance extends DurationStatus {
	public Tolerance(Character affected, int duration) {
		super("Tolerance", affected, duration);
		flag(Stsflag.tolerance);
	}

	@Override
	public String describe(Combat c) {
		if(affected.human()){
			return "You've built up a tolerance to addictive fluids.";
		}
		else{
			return affected.name()+" has built up a tolerance to your addictive fluids.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s built a tolerance to addictive fluids.\n", affected.subjectAction("have", "has"));
	}

	@Override
	public float fitnessModifier () {
		return 1;
	}
	
	@Override
	public int mod(Attribute a) {
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

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Tolerance(newAffected, getDuration());
	}
	

	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("duration", getDuration());
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new Tolerance(null, JSONUtils.readInteger(obj, "duration"));
	}
}
