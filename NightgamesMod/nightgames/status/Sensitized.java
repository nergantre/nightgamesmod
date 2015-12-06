package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.global.JSONUtils;

public class Sensitized extends DurationStatus {
	BodyPart part;
	double magnitude;
	double maximum;
	public Sensitized(Character affected, BodyPart part, double magnitude, double maximum, int duration) {
		super("Sensitized (" + part.getType() + ")", affected, duration);
		this.part = part;
		this.magnitude = magnitude;
		this.maximum = maximum;
		flag(Stsflag.sensitized);
		flag(Stsflag.purgable);
	}

	@Override
	public float fitnessModifier () {
		return (float) (-magnitude * 5);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		if (replaced)
			return "";
		return Global.format(String.format("{self:NAME-POSSESSIVE} groans as {self:possessive} %s grows hot.", part.describe(affected)), affected, c.getOther(affected));
	}

	@Override
	public String describe(Combat c) {
		return "";
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
	public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
		if (targetPart.isType(part.getType())) {
			return x * magnitude;
		}
		return 0;
	}

	public boolean overrides(Status s) {
		return s.name.equals(this.name);
	}

	public void replace(Status newStatus) {
		if (newStatus instanceof Sensitized) {
			this.magnitude = Math.min(maximum, magnitude + ((Sensitized)newStatus).magnitude);
			setDuration(Math.max(((Sensitized)newStatus).getDuration(), getDuration()));
		}
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
	public boolean lingering() {
		return true;
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Sensitized(newAffected, part, magnitude, maximum, getDuration());
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("magnitude", magnitude);
		obj.put("maximum", maximum);
		obj.put("duration", getDuration());
		obj.put("part", part.save());
		return obj;
	}

	@Override
	public Status loadFromJSON(JSONObject obj) {
		return new Sensitized(Global.noneCharacter(), Body.loadPart(obj), JSONUtils.readFloat(obj, "magnitude"), JSONUtils.readFloat(obj, "maximum"), JSONUtils.readInteger(obj, "duration"));
	}

}
