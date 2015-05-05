package status;

import java.util.HashSet;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

public class Shamed extends Status {
	private int duration;
	public Shamed(Character affected) {
		super("Shamed", affected);
		if(affected.has(Trait.PersonalInertia)){
			this.duration=6;
		}
		else{
			this.duration=4;
		}
		flag(Stsflag.shamed);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You're a little distracted by self-consciousness, and it's throwing you off your game.";
		}
		else{
			return affected.name()+" is red faced from embarrassment as much as arousal.";
		}
	}

	@Override
	public boolean mindgames(){
		return true;
	}
	
	@Override
	public float fitnessModifier () {
		return -1.0f;
	}

	@Override
	public int mod(Attribute a) {
		if(a==Attribute.Seduction || a==Attribute.Cunning){
			return Math.min(-2, -affected.getPure(a) / 5);
		}
		else{
			return 0;
		}
	}

	@Override
	public int regen(Combat c) {
		duration--;
		if(duration<=0){
			affected.removelist.add(this);
			affected.addlist.add(new Cynical(affected));
		}
		affected.emote(Emotion.nervous,20);
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
		return 1;
	}

	@Override
	public int tempted(int x) {
		return 2;
	}

	@Override
	public int evade() {
		return 0;
	}

	@Override
	public int escape() {
		return -2;
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
		return new Shamed(newAffected);
	}
}
