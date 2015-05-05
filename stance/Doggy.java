package stance;


import characters.Character;

public class Doggy extends MaledomSexStance {

	public Doggy(Character top, Character bottom) {
		super(top, bottom,Stance.doggy);
	}

	@Override
	public String describe() {
		if(top.human()){
			return bottom.name()+" is on her hands and knees in front of you, while you fuck her Doggy style.";
		}
		else{
			return "Things aren't going well for you. You're down on your hands and knees, while "+top.name()+" has free access to your manhood.";
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
		return c==top;
	}

	@Override
	public boolean reachBottom(Character c) {
		return c==top;
	}

	@Override
	public boolean prone(Character c) {
		return false;
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
		return c==top;
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
	public Position insert(Character dom, Character inserter) {
		return new Behind(top,bottom);
	}

	public Position reverse() {
		return new ReverseCowgirl(bottom, top);
	}
}
