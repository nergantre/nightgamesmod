package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public class Feral extends Status {

	public Feral(Character affected) {
		super("Feral", affected);
		flag(Stsflag.feral);
	}

	@Override
	public String describe() {
		return "";
	}

	@Override
	public float fitnessModifier () {
		return 4;
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s turned feral.\n", affected.subjectAction("have", "has"));
	}

	@Override
	public int mod(Attribute a) {
		switch(a){
		case Power:
			return 2;
		case Cunning:
			return 3;
		case Seduction:
			return 2;
		case Animism:
			return 2;
		case Speed:
			return 1;
		default:
			break;
		}
		return 0;
	}

	@Override
	public int regen(Combat c) {
		if(affected.getArousal().percent()<50){
			affected.removelist.add(this);
		}
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
		return new Feral(newAffected);
	}
}
