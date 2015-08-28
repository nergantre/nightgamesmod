package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class Rewired extends DurationStatus {
	public Rewired(Character affected, int duration) {
		super("Rewired", affected, duration);
		flag(Stsflag.rewired);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Your senses feel... wrong. It's like your sense of pleasure and pain are jumbled.";
		}
		else{
			return affected.name()+" fidgets uncertainly at the alien sensation of her rewired nerves.";
		}
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s senses is now rewired.\n", affected.nameOrPossessivePronoun());
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, double x) {
		affected.getStamina().reduce((int)Math.round(x));
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
	public boolean lingering(){
		return true;
	}

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Rewired(newAffected, getDuration());
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("duration", getDuration());
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new Rewired(null, JSONUtils.readInteger(obj, "duration"));
	}
}
