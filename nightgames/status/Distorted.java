package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class Distorted extends Status {
	private int duration;
	
	public Distorted(Character affected, int duration) {
		super("Distorted", affected);
		if(affected.has(Trait.PersonalInertia)){
			this.duration = duration * 3 / 2;
		}else{
			this.duration = duration;
		}
		flag(Stsflag.distorted);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Your image is distorted, making you hard to hit.";
		}
		else{
			return "Multiple "+affected.name()+"s appear in front of you. When you focus, you can tell which one is real, but it's still screwing up your accuracy.";
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
		affected.emote(Emotion.confident,5);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, double x) {
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
		return 10;
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
		return new Distorted(newAffected, duration);
	}
}
