package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class StoneStance extends Status {
	int duration;
	public StoneStance(Character affected) {
		super("Stone Form", affected);
		if(affected.has(Trait.PersonalInertia)){
			this.duration=15;
		}
		else{
			this.duration=10;
		}
		flag(Stsflag.form);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now in a Stone stance.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public String describe() {
		// TODO Auto-generated method stub
		return "";
	}
	
	@Override
	public float fitnessModifier () {
		return 1.0f;
	}
	
	@Override
	public int mod(Attribute a) {
		if(a==Attribute.Speed){
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
		return -x/2;
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
		return new StoneStance(newAffected);
	}
}
