package nightgames.stance;

import nightgames.characters.Character;

public class Tribadism extends Position {
	public Tribadism(Character top, Character bottom) {
		super(top, bottom, Stance.trib);
	}

	@Override
	public String describe() {
		return "Trib position: this should never display";
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
	public boolean inserted(Character c) {
		return false;
	}
	public String image() {
		return "trib.jpg";
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
		return false;
	}

	@Override
	public boolean prone(Character c) {
		return true;
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
		return new Tribadism(bottom,top);
	}
}
