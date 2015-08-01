package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class Satiated extends Status {
	int duration;
	int value;
	public Satiated(Character affected, int xp, int levels) {
		super("Satiated", affected);
		this.value = xp + 95 + (5 * (affected.getLevel() + levels));
		this.duration=1;
	}
	public Satiated(Character affected, int value) {
		super("Satiated", affected);
		this.value = value;
		this.duration=1;
	}

	@Override
	public String describe() {
		if (affected.human()) {
			return "You feel immensely powerful after feeding on your opponent's essence\n";
		} else {
			return affected.name() + " feels immensely satisfied after feeding on you essence\n";
		}
	}
	
	@Override
	public float fitnessModifier () {
		return value / 10;
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
		// TODO Auto-generated method stub
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
		return new Satiated(newAffected, value);
	}
}
