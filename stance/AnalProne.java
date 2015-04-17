package stance;


import combat.Combat;

import characters.Character;
import characters.Trait;

public class AnalProne extends Position {

	public AnalProne(Character top, Character bottom) {
		super(top, bottom, Stance.anal);
	}

	@Override
	public String describe() {
		if(top.human()){
			return String.format("You're holding %s legs over your shoulder while your cock in buried in %s's ass.",
					bottom.nameOrPossessivePronoun(), bottom.possessivePronoun());
		} else if (top.has(Trait.strapped)){
			return "You're flat on your back with your feet over your head while " + top.name() + " pegs you with her strapon dildo.";
		}
		else{
			return top.name()+" is fucking you in the ass";
		}
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
	}

	@Override
	public float priorityMod(Character self) {
		return dom(self) ? 4.0f : 0;
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
		return c==top;
	}

	@Override
	public boolean inserted(Character c) {
		return c==top;
	}

	@Override
	public Position insert(Character dom, Character inserter) {
		return new Mount(top,bottom);
	}

	public void checkOngoing(Combat c){
		if(!top.hasDick()&&!top.has(Trait.strapped)){
			if(bottom.human()){
				c.write("With "+top.name()+"'s strapon gone, your ass gets a respite.");
			}
			c.setStance(insert(top, top));
		}
	}
}
