package stance;

import combat.Combat;

import characters.Character;

public class Jumped extends FemdomSexStance {
	public Jumped(Character top, Character bottom) {
		super(top, bottom,Stance.standing);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You are clinging to "+bottom.nameOrPossessivePronoun() + " arms while her dick is buried deep in your pussy";
		}
		else{
			return top.name()+" is clinging to your shoulders and gripping your waist with her thighs while she uses the leverage to ride you.";
		}
	}

	@Override
	public boolean mobile(Character c) {
		return false;
	}

	@Override
	public boolean inserted(Character c) {
		return bottom==c;
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
		return false;
	}

	@Override
	public boolean reachBottom(Character c) {
		return false;
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
		return false;
	}

	@Override
	public boolean penetration(Character c) {
		return true;
	}

	@Override
	public Position insert(Character dom, Character inserter) {
		return new Neutral(top,bottom);
	}
	public void decay(){
		time++;
		top.weaken(null, 2);
	}

	public void checkOngoing(Combat c){
		if(top.getStamina().get()<10){
			if(top.human()){
				c.write("Your legs give out and you fall on the floor. "+bottom.name()+" lands heavily on your lap.");
				c.setStance(new Mount(bottom,top));
			}
			else{
				c.write(top.name()+" loses her balance and falls, pulling you down on top of her.");
				c.setStance(new Mount(bottom,top));
			}
		} else {
			super.checkOngoing(c);
		}
	}

	public Position reverse() {
		return new Missionary(bottom, top);
	}
}
