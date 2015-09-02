package nightgames.stance;


import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.PussyPart;

public class UpsideDownMaledom extends MaledomSexStance {
	public UpsideDownMaledom(Character top, Character bottom) {
		super(top, bottom,Stance.upsidedownmaledom);
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
	public boolean penetration(Character c) {
		return true;
	}
	
	@Override
	public boolean inserted(Character c) {
		return c==top;
	}

	@Override
	public Position insertRandom() {
		return new StandingOver(top,bottom);
	}

	public Position reverse() {
		return new UpsideDownFemdom(bottom, top);
	}

	@Override
	public BodyPart topPart() {
		return top.body.getRandomInsertable();
	}
	
	@Override
	public BodyPart bottomPart() {
		return bottom.body.getRandomPussy();
	}
}