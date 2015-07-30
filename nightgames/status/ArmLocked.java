package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;

public class ArmLocked extends Status {
	private int toughness;
	
	public ArmLocked(Character affected, int dc) {
		super("Arm Locked", affected);
		toughness = dc;
		flag(Stsflag.armlocked);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Her hands are entwined with your own, preventing your escape.";
		}
		else{
			return "Your hands are entwined with hers, preventing her escape.";
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
		return "Bound by hands";
	}

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new ArmLocked(newAffected, toughness);
	}
}
