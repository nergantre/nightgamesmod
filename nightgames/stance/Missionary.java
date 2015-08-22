package nightgames.stance;


import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.PussyPart;

public class Missionary extends MaledomSexStance {

	public Missionary(Character top, Character bottom) {
		super(top, bottom,Stance.missionary);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You are penetrating "+bottom.name()+" in traditional Missionary position.";
		}
		else{
			return top.name()+"";
		}
	}
	public String image() {
		return "missionary.jpg";
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
		return c==top;
	}

	@Override
	public Position insert() {
		return new Mount(top,bottom);
	}

	public Position reverse() {
		return new Cowgirl(bottom, top);
	}
	

	@Override
	public BodyPart topPart() {
		return top.body.getRandomInsertable();
	}
	
	@Override
	public BodyPart bottomPart() {
		return bottom.body.getRandomPussy();
	}
}