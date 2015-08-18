package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class Abuff extends Status {
	private Attribute modded;
	private int duration;
	private int value;
	public Abuff(Character affected, Attribute att, int value, int duration) {
		super(String.format("%s %+d", att.toString(), value), affected);
		this.modded=att;
		if(affected.has(Trait.PersonalInertia)){
			this.duration=3*duration/2;
		}
		else {
			this.duration=duration;
		}
		this.value=value;
	}
	
	@Override
	public String initialMessage(Combat c, boolean replaced) {
		String person, adjective, modification;
		person = affected.nameOrPossessivePronoun();

		if (Math.abs(value) > 5){
			adjective = "greatly";
		} else {
			adjective = "";
		}
		if (value > 0) {
			modification = "augmented.";
		} else {
			modification = "sapped.";
		}
		
		return String.format("%s %s is now %s %s\n", person, modded, adjective, modification);
	}

	@Override
	public float fitnessModifier () {
		return value / (2.0f * Math.min(1.0f, Math.max(1, affected.getPure(modded)) / 10.0f));
	}

	@Override
	public String describe() {
		String person, adjective, modification;

		if (affected.human()){
			person = "You feel your";
		}
		else{
			person = affected.name()+"'s";
		}
		if (Math.abs(value) > 5){
			adjective = "greatly";
		}
		else {
			adjective = "";
		}
		if (value > 0) {
			modification = "augmented.";
		} else {
			modification = "sapped.";
		}
		
		return String.format("%s %s is %s %s\n", person, modded, adjective, modification);
	}

	@Override
	public int mod(Attribute a) {
		if(a==modded){
			return value;
		}
		return 0;
	}

	@Override
	public String getVariant() {
			return modded.toString();
	}

	@Override
	public boolean overrides(Status s) {
		return false;
	}

	@Override
	public void replace(Status s) {
		assert (s instanceof Abuff);
		Abuff other = (Abuff)s;
		assert (other.modded == modded);
		this.duration = Math.max(other.duration, this.duration);
		this.value += other.value;
		this.name = String.format("%s %+d", modded.toString(), value);
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
		return new Abuff(newAffected, modded, value, duration);
	}
}
