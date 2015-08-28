package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;

public class Sore extends DurationStatus {
	public Sore(Character affected, int duration) {
		super("Sore", affected, duration);
		this.flag(Stsflag.sore);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now sore.\n", affected.subjectAction("are", "is"));
	}
	@Override
	public String describe() {
		return "";
	}

	@Override
	public float fitnessModifier () {
		return -1f;
	}
	
	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		super.regen(c);
		affected.emote(Emotion.nervous,10);
		return -1;		
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, double x) {
		return 0;
	}

	@Override
	public int weakened(int x) {
		return 0;
	}

	@Override
	public int tempted(int x) {
		return 0;
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
		return 0;
	}

	@Override
	public int spendmojo(int x) {
		return 0;
	}

	@Override
	public int counter() {
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
		return new Sore(newAffected, getDuration());
	}
}
