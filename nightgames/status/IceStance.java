package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class IceStance extends Status {
	private int duration;
	public IceStance(Character affected) {
		super("Ice Form", affected);
		if(affected.has(Trait.PersonalInertia)){
			duration = 15;
		}else{
			duration = 10;
		}
		flag(Stsflag.form);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You're as frigid as a glacier";
		}
		else{
			return affected.name()+" is cool as ice.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now in a ice stance.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public float fitnessModifier () {
		return 1;
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
		affected.emote(Emotion.dominant,5);
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
		return -x/5;
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
		return -x;
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
		return new IceStance(newAffected);
	}
}
