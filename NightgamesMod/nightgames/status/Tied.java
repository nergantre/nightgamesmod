package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public class Tied extends DurationStatus {

	public Tied(Character affected) {
		super("Tied Up", affected, 5);
		flag(Stsflag.tied);
	}

	public Tied(Character affected, int duration) {
		super("Tied Up", affected, duration);
		flag(Stsflag.tied);
	}
	
	public String describe(Combat c) {
		if (this.affected.human()) {
			return "The rope wrapped around you digs into your body, but only slows you down a bit.";
		}

		return this.affected.name()
				+ " squirms against the rope, but you know you tied it well.";
	}

	public int mod(Attribute a) {
		if (a == Attribute.Speed) {
			return -1;
		}
		return 0;
	}

	public int regen(Combat c) {
		return 0;
	}

	public int damage(Combat c, int x) {
		return 0;
	}

	public double pleasure(Combat c, double x) {
		return 0;
	}

	public int weakened(int x) {
		return 0;
	}

	public int tempted(int x) {
		return 0;
	}

	public int evade() {
		return -10;
	}

	public int escape() {
		return 0;
	}

	public int gainmojo(int x) {
		return 0;
	}

	public int spendmojo(int x) {
		return 0;
	}

	public int counter() {
		return 0;
	}

	public int value() {
		return 0;
	}

	public void turn(Combat c) {
	}

	public Status copy(Character target) {
		return new Tied(target);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("duration", getDuration());
		return obj;
	}

	@Override
	public Status loadFromJSON(JSONObject obj) {
		return new Tied(null, JSONUtils.readInteger(obj, "duration"));
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now partially tied up.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Tied(newAffected);
	}

}
