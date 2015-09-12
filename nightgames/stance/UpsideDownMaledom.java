package nightgames.stance;


import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class UpsideDownMaledom extends MaledomSexStance {
	public UpsideDownMaledom(Character top, Character bottom) {
		super(top, bottom,Stance.upsidedownmaledom);
	}

	public int pinDifficulty(Combat c, Character self) {
		return 8;
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You are holding "+bottom.name()+" upsidedown by her legs while fucking her pussy.";
		}
		else{
			return top.name()+" is holding you upsidedown by your legs while fucking your pussy.";
		}
	}
	public String image() {
		return "upsidedownmaledom.jpg";
	}
	@Override
	public boolean mobile(Character c) {
		return c==top;
	}

	@Override
	public boolean kiss(Character c) {
		return false;
	}

	@Override
	public boolean dom(Character c) {
		return c==top;
	}

	@Override
	public boolean sub(Character c) {
		return c==bottom;
	}

	@Override
	public boolean reachTop(Character c) {
		return false;
	}

	@Override
	public boolean reachBottom(Character c) {
		return true;
	}

	@Override
	public boolean prone(Character c) {
		return c==bottom;
	}

	@Override
	public boolean feet(Character c) {
		return false;
	}

	@Override
	public boolean oral(Character c) {
		return false;
	}

	@Override
	public boolean behind(Character c) {
		return false;
	}

	@Override
	public boolean inserted(Character c) {
		return c==top;
	}

	@Override
	public boolean facing() {
		return false;
	}

	@Override
	public Position insertRandom() {
		return new StandingOver(top,bottom);
	}

	public Position reverse(Combat c) {
		if (bottom.human()) {
			c.write(bottom, Global.format("Summoning your remaining strength, you hold your arms up against the floor and use your hips to tip {other:name-do} off-balance with {other:possessive} dick still held inside of you. "
					+ "{other:SUBJECT} lands on the floor with you on top of {other:direct-object} in a reverse cow-girl.", bottom, top));
		} else {
			c.write(bottom, Global.format("{self:SUBJECT} suddenly pushes against the floor and knocks you to the ground with {self:possessive} hips. "
					+ "You land on the floor with {self:direct-object} on top of you with in a reverse cow-girl position.", bottom, top));		
		}
		return new ReverseCowgirl(bottom, top);
	}
}