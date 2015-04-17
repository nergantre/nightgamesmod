package status;

import java.util.HashSet;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Trait;


public class Alluring extends Status {
	protected int duration;
	public Alluring(Character affected, int duration) {
		super("Alluring", affected);
		flag(Stsflag.alluring);
		if(affected.has(Trait.PersonalInertia)){
			this.duration=duration * 3 / 2;
		}
		else{
			this.duration=duration;
		}
	}

	public Alluring(Character affected) {
		super("Alluring", affected);
		flag(Stsflag.alluring);
		if(affected.has(Trait.PersonalInertia)){
			this.duration=4;
		}
		else{
			this.duration=3;
		}
	}

	@Override
	public String describe() {
		return affected.name()+" looks impossibly beautiful to your eyes, you can't bear to hurt her.";	
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public float fitnessModifier () {
		return 4.0f;
	}
	
	@Override
	public int regen(Combat c) {
		duration--;
		if(duration<=0){
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
		// TODO Auto-generated method stub
		return 0;
	}
}
