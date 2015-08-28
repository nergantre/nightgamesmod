package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public class Braced extends DurationStatus {

	public Braced(Character affected) {
		super("Braced", affected, 3);
		flag(Stsflag.braced);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now braced.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public String describe() {
		return "";
	}

	@Override
	public float fitnessModifier () {
		return (10.0f + (10.0f*getDuration())) / 40.f;
	}
	
	@Override
	public int mod(Attribute a) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		// TODO Auto-generated method stub
		return -x*3/4;
	}

	@Override
	public double pleasure(Combat c, double x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int weakened(int x) {
		// TODO Auto-generated method stub
		return -x*3/4;
	}

	@Override
	public int drained(int x) {
		// TODO Auto-generated method stub
		return -x*3/4;
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
		return 30 + (30 * getDuration());
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Braced(newAffected);
	}
}
