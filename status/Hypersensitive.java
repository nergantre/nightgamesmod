package status;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

public class Hypersensitive extends Status {
	private int duration;
	public Hypersensitive(Character affected) {
		super("Hypersensitive", affected);
		flag(Stsflag.hypersensitive);
		if(affected.has(Trait.PersonalInertia)){
			duration = 30;
		}else{
			duration = 20;
		}
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Your skin tingles and feels extremely sensitive to touch.";
		}
		else{
			return "She shivers from the breeze hitting her skin and has goosebumps";
		}
	}

	@Override
	public float fitnessModifier () {
		return -1;
	}

	@Override
	public int mod(Attribute a) {
		if(a == Attribute.Perception){
			return 4;
		}
		return 0;
	}
	
	@Override
	public int regen(Combat c) {
		duration--;
		if(duration<=0){
			affected.removelist.add(this);
		}
		affected.emote(Emotion.nervous,5);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public int pleasure(Combat c, int x) {
		return x/3;
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
	public boolean lingering(){
		return true;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Hypersensitive(newAffected);
	}
}
