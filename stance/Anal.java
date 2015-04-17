package stance;


import combat.Combat;

import characters.Character;
import characters.Trait;
import characters.body.AnalPussyPart;

public class Anal extends Position {

	public Anal(Character top, Character bottom) {
		super(top, bottom, Stance.anal);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You're behind " + bottom.name() + " and your cock in buried in "+bottom.possessivePronoun()+" ass.";
		}
		else if (top.has(Trait.strapped)){
			return top.name()+" is pegging you with her strapon dildo from behind.";
		}
		else{
			return top.name()+" is fucking you in the ass from behind";
		}
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
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
		return c==top;
	}

	@Override
	public boolean reachBottom(Character c) {
		return c==top;
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
		return c==top;
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
	public float priorityMod(Character self) {
		float priority = 0;
		if (dom(self)) {
			priority += 4;
		}
		if (sub(self) && self.body.getRandom("ass") instanceof AnalPussyPart) {
			priority += 4;
		}
		return priority;
	}

	@Override
	public Position insert(Character dom, Character inserter) {
		return new Behind(top,bottom);
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
