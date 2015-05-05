package status;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

public class WaterStance extends Status {
	int duration;
	public WaterStance(Character affected) {
		super("Water Form", affected);
		flag(Stsflag.form);
		if(affected.has(Trait.PersonalInertia)){
			this.duration=15;
		}
		else{
			this.duration=10;
		}
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You're as smooth and responsive as flowing water.";
		}
		else{
			return affected.name()+" continues her flowing movements.";
		}
	}
	
	@Override
	public float fitnessModifier () {
		return 1.0f;
	}

	@Override
	public int mod(Attribute a) {
		if(Attribute.Power==a){
			return -2;
		}
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int pleasure(Combat c, int x) {
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
		return 10;
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
		return 5;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new WaterStance(newAffected);
	}
}
