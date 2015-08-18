package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class Nimble extends Status {
	private int duration;

	public Nimble(Character affected, int duration) {
		super("Nimble", affected);
		if(affected.has(Trait.PersonalInertia)){
			this.duration=3*duration/2;
		}
		else{
			this.duration=duration;
		}
		flag(Stsflag.nimble);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You're as quick and nimble as a cat.";
		}
		else{
			return affected.name()+" darts around gracefully.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now more nimble.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public float fitnessModifier () {
		return affected.get(Attribute.Animism) / 10.0f;
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
		affected.emote(Emotion.confident,5);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		// TODO Auto-generated method stub
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
		return affected.get(Attribute.Animism)*affected.getArousal().percent()/100;
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
		return affected.get(Attribute.Animism)*affected.getArousal().percent()/100;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Nimble(newAffected, duration);
	}
}
