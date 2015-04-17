package stance;

import combat.Combat;

import characters.Character;

public class Standing extends Position {
	public Standing(Character top, Character bottom) {
		super(top, bottom,Stance.standing);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You are holding "+bottom.name()+" in the air while buried deep in her pussy";
		}
		else{
			return top.name()+" is holding you in her arms while pumping her " + top.body.getRandomInsertable().describe(top) + " into your girl parts.";
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
		}
	}
	@Override
	public float priorityMod(Character self) {
		float priority = 0;
		if (dom(self)) {
			priority += 4;
		}
		if (sub(self)) {
			priority += self.body.getRandomPussy().priority;
		}
		return priority;
	}
}
