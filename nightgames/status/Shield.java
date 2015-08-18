package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class Shield extends Status {
	private int duration;
	private double strength;

	public Shield(Character affected, double strength) {
		this(affected, strength, 4);
	}
		
	public Shield(Character affected, double strength, int duration) {
		super("Shield", affected);
		if(affected.has(Trait.PersonalInertia)){
			this.duration=duration * 3 / 2;
		}
		else{
			this.duration=duration;
		}
		this.strength = strength;
		flag(Stsflag.shielded);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now shielded.\n", affected.subjectAction("are", "is"));
	}
	@Override
	public String describe() {
		return "";
	}

	@Override
	public float fitnessModifier () {
		return (float)strength * 4;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		duration--;
		if(duration<0){
			affected.removelist.add(this);
		}
		affected.emote(Emotion.confident,5);
		return 0;		
	}

	@Override
	public int damage(Combat c, int x) {
		return (int) - Math.round(x * strength);
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
		return 2;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Shield(newAffected, strength, duration);
	}
}
