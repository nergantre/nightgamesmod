package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class Horny extends DurationStatus {
	private float magnitude;
	private String source;

	public Horny(Character affected, float magnitude, int duration, String source) {
		super("Horny", affected, duration);
		this.source = source;
		this.magnitude = magnitude;
		flag(Stsflag.horny);
	}

	public String toString() {
		return "Aroused from " + source + " ("+ magnitude +" x "+ getDuration() + ")";
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Your heart pounds in your chest as you try to surpress your arousal from contacting " + source + ".";
		}
		else{
			return affected.name()+" is flushed and her nipples are noticeably hard from contacting " + source + ".";
		}
	}

	@Override
	public float fitnessModifier () {
		return -Math.min(.5f, magnitude * getDuration() / 3.0f);
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		super.regen(c);
		return 0;
	}

	@Override
	public void tick(Combat c) {
		affected.arouse(Math.round(magnitude), c, " ("+ source +")");
		affected.emote(Emotion.horny,20);		
	}

	@Override
	public String getVariant() {
			return source;
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now aroused by %s.\n", affected.subjectAction("are", "is"), source + " ("+ magnitude +" x "+ getDuration()+ ")");
	}

	@Override
	public boolean overrides(Status s) {
		return false;
	}

	@Override
	public void replace(Status s) {
		assert (s instanceof Horny);
		Horny other = (Horny)s;
		assert (other.source.equals(source));
		setDuration(Math.max(other.getDuration(), getDuration()));
		this.magnitude += other.magnitude;
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
	public boolean lingering(){
		return true;
	}

	@Override
	public int value() {
		return 0;
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Horny(newAffected, magnitude, getDuration(), source);
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("source", source);
		obj.put("magnitude", magnitude);
		obj.put("duration", getDuration());
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new Horny(null, 
				JSONUtils.readFloat(obj, "magnitude"),
				JSONUtils.readInteger(obj, "duration"),
				JSONUtils.readString(obj, "source"));
	}
}
