package status;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Emotion;

public class LegLocked extends Status {
	private int toughness;
	
	public LegLocked(Character affected, int dc) {
		super("Leg Locked", affected);
		toughness = dc;
		flag(Stsflag.leglocked);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Her legs are locked around your waist, preventing you from pulling out.";
		}
		else{
			return "Your legs are wrapped around her waist, preventing her from pulling out.";
		}
	}

	@Override
	public float fitnessModifier () {
		return -toughness / 10.0f;
	}

	@Override
	public int mod(Attribute a) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int regen(Combat c) {
		if (!c.getStance().inserted(affected) || toughness <= 0.01) {
			affected.removelist.add(this);
		}
		affected.emote(Emotion.horny, 10);
		toughness -= 2;
		return 0;
	}
	@Override
	public int damage(Combat c, int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int pleasure(Combat c, int x) {
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
		return -15;
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
		return -10;
	}
	public String toString(){
		return "Bound by legs";
	}

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new LegLocked(newAffected, toughness);
	}
}
