package status;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

public class Shield extends Status {
	private int duration;
	public Shield(Character affected) {
		super("Shield", affected);
		if(affected.has(Trait.PersonalInertia)){
			this.duration=6;
		}
		else{
			this.duration=4;
		}
		flag(Stsflag.shielded);
	}

	@Override
	public String describe() {
		return "";
	}

	@Override
	public float fitnessModifier () {
		return .5f;
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
		return -4;
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
		return 2;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}

}
