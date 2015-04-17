package stance;


import characters.Character;
import characters.body.AnalPussyPart;
import characters.body.PussyPart;

public class Cowgirl extends Position {

	public Cowgirl(Character top, Character bottom) {
		super(top, bottom,Stance.cowgirl);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You're on top of "+bottom.name()+".";
		}
		else{
			return top.name()+" is riding you in Cowgirl position. Her breasts bounce in front of your face each time she moves her hips.";
		}
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
	}

	@Override
	public boolean kiss(Character c) {
		return true;
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
		return true;
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
		return c==bottom;
	}

	@Override
	public Position insert(Character dom, Character inserter) {
		return new Mount(top,bottom);
	}

	@Override
	public float priorityMod(Character self) {
		float priority = 0;
		if (dom(self)) {
			priority += 4;
			priority += self.body.getRandomPussy().priority;
		}
		return priority;
	}
}
