package nightgames.stance;


import nightgames.characters.Character;

public class ReverseMount extends Position {

	public ReverseMount(Character top, Character bottom) {
		super(top, bottom,Stance.reversemount);
		
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You are straddling "+bottom.name()+", with your back to her.";
		}
		else{
			return top.name()+" is sitting on your chest, facing your groin.";
		}
	}
	@Override
	public Position insert(Character target) {
		if (target == top) {
			return insert();
		} else {
			return reverse().insert();
		}
	}
	@Override
	public boolean mobile(Character c) {
		return c==top;
	}
	public String image() {
		if (bottom.hasPussy()) {
			return "mount_m.jpg";
		} else {
			return "mount_f.jpg";
		}
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
		return false;
	}
	@Override
	public boolean inserted(Character c) {
		return false;
	}

	@Override
	public boolean penetration(Character c) {
		return false;
	}

	@Override
	public Position insert() {
		if(top.hasDick() && bottom.hasPussy()){
			return new Missionary(top,bottom);
		}
		else {
			return new ReverseCowgirl(top,bottom);
		}
	}
	@Override
	public float priorityMod(Character self) {
		return getSubDomBonus(self, 4.0f);
	}
}
