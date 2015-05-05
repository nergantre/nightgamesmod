package status;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

public class Buzzed extends Status {
	private int duration;
	public Buzzed(Character affected) {
		super("Buzzed", affected);
		if(affected.has(Trait.PersonalInertia)){
			duration = 30;
		}else{
			duration = 20;
		};
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You feel a pleasant buzz, which makes you a bit sluggish, but also takes the edge of your sense of touch.";
		}
		else{
			return affected.name()+" looks mildly buzzed, probably trying to dull her senses.";
		}
	}
	
	@Override
	public float fitnessModifier () {
		return 0.0f;
	}

	@Override
	public int mod(Attribute a) {
		if(a == Attribute.Perception){
			return -3;
		}
		else if(a == Attribute.Power){
			return -1;
		}
		else if(a == Attribute.Cunning){
			return -2;
		}
		return 0;
	}

	@Override
	public int regen(Combat c) {
		duration--;
		if(duration<=0){
			affected.removelist.add(this);
		}
		affected.emote(Emotion.confident,15);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int pleasure(Combat c, int x) {
		return -x/10;
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
		return -5;
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
		return new Buzzed(newAffected);
	}
}
