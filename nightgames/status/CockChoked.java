package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class CockChoked extends Status {
	int duration;
	Character other;
	
	public CockChoked(Character affected, Character other, int duration) {
		super("Cock Choked", affected);
		this.duration = duration;
		this.other = other;
		flag(Stsflag.orgasmseal);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now preventing %s from cumming\n", other.subjectAction("are", "is"), affected.subject());
	}

	@Override
	public String describe() {
		return String.format("%s preventing %s from cumming\n", other.subjectAction("are", "is"), affected.subject());
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
		duration -= 1;
		if(duration<=0 || !c.getStance().inserted(affected)){
			affected.removelist.add(this);
			affected.addlist.add(new Wary(affected, 2));
		} else {
			if (affected.getArousal().percent() > 80) {
				affected.emote(Emotion.desperate, 10);
				affected.emote(Emotion.horny, 10);
			}
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
	public void struggle(Character self) {
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
		return "Cock Choked";
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new CockChoked(newAffected, newOther, duration);
	}
}
