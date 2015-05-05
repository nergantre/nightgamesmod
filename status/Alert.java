package status;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

public class Alert extends Status {
	private int duration;
	
	public Alert(Character affected) {
		super("Alert", affected);
		if(affected.has(Trait.PersonalInertia)){
			duration = 5;
		}else{
			duration = 3;
		}
		flag(Stsflag.alert);
	}
	
	@Override
	public float fitnessModifier () {
		return 3;
	}

	@Override
	public String describe() {
		return "";
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
		return 5;
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
		return 15;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Alert(newAffected);
	}
}
