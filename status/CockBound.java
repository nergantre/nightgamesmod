package status;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Emotion;

public class CockBound extends Status {
	private int toughness;
	private String binding;
	
	public CockBound(Character affected, int dc, String binding) {
		super("Cock Bound", affected);
		toughness = dc;
		this.binding = binding;
		flag(Stsflag.cockbound);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Your dick is bound by "+binding+".";
		}
		else{
			return "Her girl-cock is restrained by "+binding+".";
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
		if (!c.getStance().inserted(affected)) {
			affected.removelist.add(this);
		}
		affected.emote(Emotion.desperate, 10);
		affected.emote(Emotion.nervous, 10);
		affected.emote(Emotion.horny, 20);
		toughness -= 1;
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
		int dc = toughness;
		toughness-=2;
		return -dc * 10;
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
		return "Bound by " + binding;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}
}
