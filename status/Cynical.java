package status;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Trait;

public class Cynical extends Status {
	int duration;
	
	public Cynical(Character affected) {
		super("Cynical", affected);
		flag(Stsflag.cynical);
		if(affected.has(Trait.PersonalInertia)){
			duration = 5;
		}else{
			duration = 3;
		}
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You're feeling more cynical than usual and won't fall for any mind games.";
		}
		else{
			return affected.name()+" has a cynical edge in her eyes.";
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
		if(duration<=0){
			affected.removelist.add(this);
		}
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
		return 0;
	}

	@Override
	public int tempted(int x) {
		return -5;
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
		return -x;
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
}
