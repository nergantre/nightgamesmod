package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class Standing extends MaledomSexStance {
	public Standing(Character top, Character bottom) {
		super(top, bottom,Stance.standing);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You are holding "+bottom.name()+" in the air while buried deep in her pussy";
		}
		else{
			return top.name()+" is holding you in her arms while pumping into your girl parts.";
		}
	}

	@Override
	public boolean mobile(Character c) {
		return false;
	}
	@Override
	public boolean inserted(Character c) {
		return top==c;
	}
	@Override
	public boolean kiss(Character c) {
		return true;
	}
	
	public String image() {
		return "standing.jpg";
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
	public Position insert() {
		return new Neutral(top,bottom);
	}
	public void decay(Combat c){
		time++;
		top.weaken(null, 2);
	}

	public void checkOngoing(Combat c){
		if(top.getStamina().get()<10){
			if(top.human()){
				c.write("Your legs give out and you fall on the floor. "+bottom.name()+" lands heavily on your lap.");
				c.setStance(new Cowgirl(bottom,top));
			}
			else{
				c.write(top.name()+" loses her balance and falls, pulling you down on top of her.");
				c.setStance(new Cowgirl(bottom,top));
			}
		} else {
			super.checkOngoing(c);
		}
	}

	public Position reverse() {
		return new Cowgirl(bottom, top);
	}
}
