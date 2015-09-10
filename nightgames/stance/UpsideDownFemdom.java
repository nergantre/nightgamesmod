package nightgames.stance;


import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.PussyPart;

public class UpsideDownFemdom extends FemdomSexStance {
	public UpsideDownFemdom(Character top, Character bottom) {
		super(top, bottom,Stance.upsidedownfemdom);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You are holding "+bottom.name()+" upsidedown by her legs while fucking her cock with your slit.";
		}
		else{
			return top.name()+" is holding you upsidedown by your legs while fucking your cock with her slit.";
		}
	}

	public String image() {
		return "upsidedownfemdom.jpg";
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
	public Position insertRandom() {
		return new StandingOver(top,bottom);
	}

	public Position reverse() {
		return new UpsideDownMaledom(bottom, top);
	}
}