package status;

import java.util.HashSet;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Trait;


public class Unreadable extends Status {
	protected int duration;
	public Unreadable(Character affected) {
		super("Unreadable", affected);
		flag(Stsflag.unreadable);
		if(affected.has(Trait.PersonalInertia)){
			this.duration=5;
		}
		else{
			this.duration=3;
		}
	}

	@Override
	public String describe() {
		return null;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public float fitnessModifier () {
		return .2f;
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
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Unreadable(newAffected);
	}
}
