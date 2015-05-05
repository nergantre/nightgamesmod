package status;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Trait;

public class Rewired extends Status {
	private int duration;
	
	public Rewired(Character affected, int duration) {
		super("Rewired", affected);
		if(affected.has(Trait.PersonalInertia)){
			this.duration=3*duration/2;
		}
		else{
			this.duration=duration;
		}
		flag(Stsflag.rewired);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Your senses feel... wrong. It's like your sense of pleasure and pain are jumbled.";
		}
		else{
			return affected.name()+" fidgets uncertainly at the alien sensation of her rewired nerves.";
		}
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
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		affected.getArousal().restore(x);
		return -x;
	}

	@Override
	public int pleasure(Combat c, int x) {
		affected.getStamina().reduce(x);
		return -x;
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
	public boolean lingering(){
		return true;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Rewired(newAffected, duration);
	}
}
