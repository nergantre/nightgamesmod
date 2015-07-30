package stance;


import characters.Character;

public class Neutral extends Position {

	public Neutral(Character top, Character bottom) {
		super(top, bottom,Stance.neutral);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You and "+bottom.name()+" circle each other cautiously";
		}
		else{
			return "You and "+top.name()+" circle each other cautiously";
		}
	}
	
	public String image() {
			return "neutral.jpg";
	}
	
	@Override
	public boolean inserted(Character c) {
		return false;
	}
	@Override
	public boolean mobile(Character c) {
		return true;
	}

	@Override
	public boolean kiss(Character c) {
		return true;
	}

	@Override
	public boolean dom(Character c) {
		return false;
	}

	@Override
	public boolean sub(Character c) {
		return false;
	}

	@Override
	public boolean reachTop(Character c) {
		return true;
	}

	@Override
	public boolean reachBottom(Character c) {
		return true;
	}

	@Override
	public boolean prone(Character c) {
		return false;
	}

	@Override
	public boolean feet(Character c) {
		return true;
	}

	@Override
	public boolean oral(Character c) {
		return true;
	}

	@Override
	public boolean behind(Character c) {
		return false;
	}

	@Override
	public boolean penetration(Character c) {
		return false;
	}

	@Override
	public Position insert(Character dom) {
		Character other = getOther(dom);
		if(dom.hasDick() && other.hasPussy()){
			return new Standing(dom,other);
		} else if(other.hasDick() && dom.hasPussy()) {
			return new Jumped(dom,other);
		} else {
			return this;
		}
	}
}
