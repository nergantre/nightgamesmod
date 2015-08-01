package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;

public class CockBound extends Status {
	private int toughness;
	public String binding;
	
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
		return -15;
	}

	@Override
	public int escape() {
		int dc = toughness;
		return -dc * 10;
	}

	@Override
	public void struggle(Character self) {
		toughness = Math.round(toughness * .5f);
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
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new CockBound(newAffected, toughness, binding);
	}
}
