package nightgames.status;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.skills.Anilingus;
import nightgames.skills.Blowjob;
import nightgames.skills.BreastWorship;
import nightgames.skills.CockWorship;
import nightgames.skills.FootWorship;
import nightgames.skills.Grind;
import nightgames.skills.Invitation;
import nightgames.skills.Piston;
import nightgames.skills.PussyWorship;
import nightgames.skills.ReverseAssFuck;
import nightgames.skills.ReverseCarry;
import nightgames.skills.ReverseFly;
import nightgames.skills.Skill;
import nightgames.skills.SpiralThrust;
import nightgames.skills.Thrust;
import nightgames.skills.WildThrust;

public class BodyFetish extends DurationStatus {
	Character		origin;
	public String	part;
	public double	magnitude;

	public BodyFetish(Character affected, Character origin, String part,
			double magnitude) {
		super(Global.capitalizeFirstLetter(part) + " Fetish", affected, 10);
		flag(Stsflag.bodyfetish);
		this.origin = origin;
		this.part = part;
		this.magnitude = magnitude;
	}

	@Override
	public boolean lingering() {
		return true;
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		if (replaced) {
			return String.format("%s %s fetish has grown.\n",
					affected.nameOrPossessivePronoun(), part);
		} else {
			return String.format("%s now affected by a %s fetish.\n",
					affected.subjectAction("are", "is"), part);
		}
	}

	@Override
	public String describe(Combat c) {
		String desc = "";
		if (magnitude < .26) {
			desc = "a slight ";
		} else if (magnitude < .51) {
			desc = "";
		} else if (magnitude < .99) {
			desc = "a fierce ";
		} else {
			desc = "an overwhelming ";
		}
		String magString = Global.formatDecimal(magnitude);
		if (affected.human()) {
			if (origin != null && c != null && c.getOther(affected) == origin) {
				return Global.capitalizeFirstLetter(desc
						+ "fantasies of worshipping "
						+ origin.nameOrPossessivePronoun() + " " + part
						+ " run through your mind (" + magString + ").");
			} else {
				return Global.capitalizeFirstLetter(desc
						+ "fantasies of worshipping " + part
						+ " run through your mind (" + magString + ").");
			}
		} else {
			return affected.name() + " is affected by " + desc + part
					+ " fetish (" + magString + ").";
		}
	}

	@Override
	public Collection<Skill> allowedSkills(Combat c) {
		if (magnitude <= .99) {
			return Collections.emptySet();
		} else if (part.equals("pussy")) {
			return Arrays.asList((Skill) new PussyWorship(affected));
		} else if (part.equals("breasts")) {
			return Arrays.asList((Skill) new BreastWorship(affected));
		} else if (part.equals("feet")) {
			return Arrays.asList((Skill) new FootWorship(affected));
		} else if (part.equals("ass")) {
			return Arrays.asList((Skill) new Anilingus(affected));
		} else if (part.equals("cock")) {
			return Arrays.asList((Skill) new Blowjob(affected),
					new ReverseAssFuck(affected), new ReverseFly(affected),
					new ReverseCarry(affected), new Invitation(affected),
					new Thrust(affected), new Piston(affected),
					new Grind(affected), new SpiralThrust(affected),
					new CockWorship(affected), new WildThrust(affected));
		} else {
			return Collections.emptySet();
		}
	}

	@Override
	public float fitnessModifier() {
		return -(float) magnitude * 3;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public boolean overrides(Status s) {
		return false;
	}

	@Override
	public void replace(Status s) {
		assert s instanceof BodyFetish;
		BodyFetish other = (BodyFetish) s;
		assert other.part.equals(part);
		magnitude = Math.min(3.0, magnitude + other.magnitude);
		this.setDuration(Math.max(getDuration(), other.getDuration()));
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
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
		return new BodyFetish(newAffected, newOther, part, magnitude);
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("part", part);
		obj.put("magnitude", magnitude);
		return obj;
	}

	@Override
	public Status loadFromJSON(JSONObject obj) {
		return new BodyFetish(null, null, JSONUtils.readString(obj, "part"),
				JSONUtils.readFloat(obj, "magnitude"));
	}

	@Override
	public int regen(Combat c) {
		if (magnitude > .25) {
			magnitude = Math.max(.25, magnitude - .02);
		}
		return 0;
	}
}
