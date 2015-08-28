package nightgames.status;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Anilingus;
import nightgames.skills.Blowjob;
import nightgames.skills.FootWorship;
import nightgames.skills.Grind;
import nightgames.skills.Invitation;
import nightgames.skills.Piston;
import nightgames.skills.ReverseAssFuck;
import nightgames.skills.ReverseCarry;
import nightgames.skills.ReverseFly;
import nightgames.skills.Skill;
import nightgames.skills.SpiralThrust;
import nightgames.skills.Thrust;

public class BodyFetish extends DurationStatus {
	Character origin;
	public String part;
	public double magnitude;

	public BodyFetish(Character affected, Character origin, String part, double magnitude, int duration) {
		super(Global.capitalizeFirstLetter(part) + " Fetish", affected, duration);
		flag(Stsflag.bodyfetish);
		this.origin = origin;
		this.part = part;
		this.magnitude = magnitude;
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now affected by a %s fetish.\n", affected.subjectAction("are", "is"), part);
	}

	@Override
	public String describe() {
		String desc = "";
		if (magnitude < .24) {
			desc = "brief ";
		} else if (magnitude < .49) {
			desc = "";
		} else if (magnitude < .99) {
			desc = "fierce ";
		} else {
			desc = "overwhelming ";
		}
		if(affected.human()){
			return Global.capitalizeFirstLetter(desc + "fantasies of worshiping " + origin.nameOrPossessivePronoun() + " " + part + " run through your mind (" + magnitude +").");
		}
		else{
			return affected.name()+" is affected by a " + desc + part + " fetish (" + magnitude +").";
		}
	}

	@Override
	public Collection<Skill> allowedSkills(){
		if (magnitude <= .99) {
			return Collections.emptySet();
		} else if (part.equals("feet")) {
			return Arrays.asList((Skill)
					new FootWorship(affected));
		} else if (part.equals("ass")) {
			return Arrays.asList((Skill)
					new Anilingus(affected));
		} else if (part.equals("cock")) {
			return Arrays.asList((Skill)
					new Blowjob(affected),
					new ReverseAssFuck(affected),
					new ReverseFly(affected),
					new ReverseCarry(affected),
					new Invitation(affected),
					new Thrust(affected),
					new Piston(affected),
					new Grind(affected),
					new SpiralThrust(affected));	
		} else {
			return Collections.emptySet();
		}
	}

	@Override
	public float fitnessModifier () {
		return -(float)magnitude * 3;
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
		assert (s instanceof BodyFetish);
		BodyFetish other = (BodyFetish)s;
		assert (other.part.equals(part));
		setDuration(Math.max(other.getDuration(), this.getDuration()));
		this.magnitude = Math.min(2.5, this.magnitude + other.magnitude);
	}

	@Override
	public int damage(Combat c, int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double pleasure(Combat c, double x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int weakened(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int tempted(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evade() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int escape() {
		return 0;
	}

	@Override
	public int gainmojo(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int spendmojo(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int counter() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new BodyFetish(newAffected, newOther, part, magnitude, getDuration());
	}
}
