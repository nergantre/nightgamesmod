package nightgames.stance;


import nightgames.characters.Character;
import nightgames.combat.Combat;

public class Pin extends Position {

	public Pin(Character top, Character bottom) {
		super(top, bottom,Stance.pin);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You're sitting on "+bottom.name()+", holding her arms in place.";
		}
		else{
			return top.name()+" is pinning you down, leaving you helpless.";
		}		
	}

	@Override
	public int pinDifficulty(Combat c, Character self) {
		return 10;
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
	}
	public String image() {
		return "pin.jpg";
	}
	@Override
	public boolean kiss(Character c) {
		return c==top;
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
		return c==bottom;
	}
	@Override
	public boolean inserted(Character c) {
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
		return false;
	}

	@Override
	public Position insert(Character dom) {
		Character other = getOther(dom);

		if(dom.hasDick()&&other.hasPussy()){
			return new Missionary(dom,other);
		}
		else if(dom.hasPussy()&&bottom.hasDick()){
			return new Cowgirl(dom,other);
		}
		else{
			return this;
		}
	}
	
	@Override
	public float priorityMod(Character self) {
		return getSubDomBonus(self, 2.0f);
	}
}
