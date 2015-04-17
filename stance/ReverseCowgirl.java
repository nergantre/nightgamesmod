package stance;


import characters.Character;

public class ReverseCowgirl extends Position {

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
	public boolean penetration(Character c) {
		return true;
	}
	@Override
	public boolean inserted(Character c) {
		return c==bottom;
	}
	@Override
	public Position insert(Character dom, Character inserter) {
		return new ReverseMount(top,bottom);
	}
	@Override
	public float priorityMod(Character self) {
		return dom(self) ? 4.0f : 0;
	}
}
