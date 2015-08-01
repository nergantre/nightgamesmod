package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class BodyFetish extends Status{
	private int duration;
	Character origin;
	public String part;
	public double magnitude;

	public BodyFetish(Character affected, Character origin, String part, double magnitude, int duration) {
		super(Global.capitalizeFirstLetter(part) + " Fetish", affected);
		flag(Stsflag.bodyfetish);
		if(affected.has(Trait.PersonalInertia)){
			this.duration = duration * 3 / 2;
		}else{
			this.duration = duration;
		}
		this.origin = origin;
		this.part = part;
		this.magnitude = magnitude;
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
	public float fitnessModifier () {
		return -(float)magnitude * 3;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		if(duration<=0){
			affected.removelist.add(this);
		}
		duration--;
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
		this.duration = Math.max(other.duration, this.duration);
		this.magnitude += other.magnitude;
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
		return new BodyFetish(newAffected, newOther, part, magnitude, duration);
	}
}
