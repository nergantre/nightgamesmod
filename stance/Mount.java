package stance;


import characters.Character;

public class Mount extends Position {

	public Mount(Character top, Character bottom) {
		super(top, bottom,Stance.mount);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You're on top of "+bottom.name()+".";
		}
		else{
			return top.name()+" is straddling you, with her enticing breasts right in front of you.";
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
		return false;
	}
	
	@Override
	public boolean inserted(Character c) {
		return false;
	}

	@Override
	public Position insert(Character dom, Character inserter) {
		if(dom == inserter){
			return new Missionary(top,bottom);
		}
		else {
			return new Cowgirl(top,bottom);
		}
	}
	@Override
	public float priorityMod(Character self) {
		return dom(self) ? 4.0f : 0;
	}
}
