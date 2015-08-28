package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;

public class StoneStance extends DurationStatus {
	public StoneStance(Character affected) {
		super("Stone Form", affected, 10);
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
			return -1;
		}
		return 0;
	}

	@Override
	public int regen(Combat c) {
		super.regen(c);
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
