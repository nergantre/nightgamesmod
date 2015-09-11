package nightgames.stance;


import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;

public class ReverseCowgirl extends FemdomSexStance {

	public ReverseCowgirl(Character top, Character bottom) {
		super(top, bottom,Stance.reversecowgirl);
		
	}

	@Override
	public String describe() {
		if(top.human()){
			return "";
		}
		else{
			return top.name()+" is riding you in Reverse Cowgirl position, facing your feet.";
		}
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
	}
	public String image() {
		return "reverse_cowgirl.jpg";
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
		return c==bottom;
	}

	@Override
	public boolean reachBottom(Character c) {
		return c==top;
	}

	@Override
	public boolean prone(Character c) {
		return c==bottom;
	}

	@Override
	public boolean feet(Character c) {
		return c==top;
	}

	@Override
	public boolean oral(Character c) {
		return c==top;
	}

	@Override
	public boolean behind(Character c) {
		return c==bottom;
	}

	@Override
	public Position insertRandom() {
		return new ReverseMount(top,bottom);
	}

	public Position reverse() {
		return new Doggy(bottom, top);
	}
}
