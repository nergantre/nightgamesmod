package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;

public class Buzzed extends DurationStatus {
	
	private int magnitude;
	
	public Buzzed(Character affected) {
		super("Buzzed", affected, 20);
		magnitude = 1;
	}

	@Override
	public String describe(Combat c) {
		if(affected.human()){
			return "You feel a pleasant buzz, which makes you a bit sluggish, but also takes the edge of your sense of touch.";
		}
		else{
			return affected.name()+" looks mildly buzzed, probably trying to dull her senses.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now buzzed.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public float fitnessModifier () {
		return 0.0f;
	}

	@Override
	public int mod(Attribute a) {
		if(a == Attribute.Perception){
			return -3 * magnitude;
		}
		else if(a == Attribute.Power){
			return -magnitude;
		}
		else if(a == Attribute.Cunning){
			return -2 * magnitude;
		}
		return 0;
	}

	@Override
	public int regen(Combat c) {
		super.regen(c);
		affected.emote(Emotion.confident,15);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, double x) {
		return -x/(10 / magnitude);
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
		return -5 * magnitude;
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
	public boolean lingering() {
		return true;
	}
	
	@Override
	public void replace(Status newStatus) {
		assert newStatus instanceof Buzzed;
		Buzzed other = (Buzzed) newStatus;
		setDuration(Math.max(other.getDuration(), getDuration()));
		this.magnitude += other.magnitude;		
	}
	
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Buzzed(newAffected);
	}
	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new Buzzed(null);
	}
}
