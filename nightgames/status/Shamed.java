package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;

public class Shamed extends DurationStatus {
	public Shamed(Character affected) {
		super("Shamed", affected, 4);
		flag(Stsflag.shamed);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You're a little distracted by self-consciousness, and it's throwing you off your game.";
		}
		else{
			return affected.name()+" is red faced from embarrassment as much as arousal.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now shamed.\n", affected.subjectAction("are", "is"));
	}
	@Override
	public boolean mindgames(){
		return true;
	}
	
	@Override
	public float fitnessModifier () {
		return -1.0f;
	}

	@Override
	public int mod(Attribute a) {
		if(a==Attribute.Seduction || a==Attribute.Cunning){
			return Math.min(-2, -affected.getPure(a) / 5);
		}
		else{
			return 0;
		}
	}

	@Override
	public int regen(Combat c) {
		super.regen(c);
		affected.emote(Emotion.nervous,20);
		affected.spendMojo(c, 5);
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
		return 1;
	}

	@Override
	public int tempted(int x) {
		return 2;
	}

	@Override
	public int evade() {
		return 0;
	}

	@Override
	public int escape() {
		return -2;
	}

	@Override
	public int gainmojo(int x) {
		return -x/2;
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
		return new Shamed(newAffected);
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new Shamed(null);
	}
}
