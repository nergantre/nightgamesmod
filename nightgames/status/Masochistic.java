package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class Masochistic extends Status {
	private int duration;
	public Masochistic(Character affected) {
		super("Masochism", affected);
		flag(Stsflag.masochism);
		if(affected.has(Trait.PersonalInertia)){
			duration = 15;
		}else{
			duration = 10;
		}
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Arousing fantasies of being hurt continue to tempt you.";
		}
		else{
			return affected.name()+" is still flushed with arousal at the idea of being struck.";
		}
	}

	@Override
	public float fitnessModifier () {
		return -.5f;
	}

	@Override
	public int mod(Attribute a) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int regen(Combat c) {
		duration--;
		if(duration<0){
			affected.removelist.add(this);
		}
		affected.emote(Emotion.nervous,15);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		affected.arouse(x / 2, c);
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
		return new Masochistic(newAffected);
	}
}
