package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class Tolerance extends Status {
	private int duration;

	public Tolerance(Character affected, int duration) {
		super("Tolerance", affected);
		this.duration=duration;
		flag(Stsflag.tolerance);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You've built up a tolerance to addictive fluids.";
		}
		else{
			return affected.name()+" has built up a tolerance to your addictive fluids.";
		}
	}

	@Override
	public float fitnessModifier () {
		return 1;
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
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public int pleasure(Combat c, int x) {
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
		return new Tolerance(newAffected, duration);
	}
}
