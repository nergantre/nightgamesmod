package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class OrgasmSeal extends Status {
	int duration;
	public OrgasmSeal(Character affected, int duration) {
		super("Orgasm Sealed", affected);
		this.duration = duration;
		flag(Stsflag.orgasmseal);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s ability to cum is now sealed!\n", affected.subject());
	}

	@Override
	public String describe() {
		if(affected.hasBalls()) {
			return Global.format("A pentragram on {self:name-possessive} ballsack glows with a sinister light.", affected, affected);
		} else {
			return Global.format("A pentragram on {self:name-possessive} lower belly glows with a sinister light.", affected, affected);
		}
	}

	@Override
	public float fitnessModifier () {
		if (affected.getArousal().percent() > 80) {
			return -10;
		}
		return 0;
	}

	@Override
	public int mod(Attribute a) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int regen(Combat c) {
		if (affected.getArousal().isFull()) {
			duration -= 5;
		} else {
			duration -= 1;
		}
		if(duration<=0){
			affected.removelist.add(this);
			affected.addlist.add(new Wary(affected, 2));
		}
		if (affected.getArousal().percent() > 80) {
			affected.emote(Emotion.desperate, 10);
			affected.emote(Emotion.horny, 10);
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
	public String toString(){
		return "Orgasm Sealed";
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new OrgasmSeal(newAffected, duration);
	}
}
