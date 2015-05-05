package stance;


import status.Stsflag;
import combat.Combat;

import characters.Character;
import characters.Trait;
import characters.body.AnalPussyPart;

public class Anal extends AnalSexStance {

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
	public Position insert(Character dom, Character inserter) {
		return new Behind(top,bottom);
	}

	public void checkOngoing(Combat c){
		Character inserter = inserted(top) ? top : bottom;
		Character inserted = inserted(top) ? bottom : top;
		
		if(!inserter.hasDick()&&!inserter.has(Trait.strapped)){
			if(inserted.human()){
				c.write("With "+inserter.name()+"'s pole gone, your ass gets a respite.");
			} else {
				c.write(inserted.name() + " sighs with relief with your dick gone.");
			}
			c.setStance(insert(top, bottom));
		}
		if (inserted.body.getRandom("ass") == null) {
			if(inserted.human()){
				c.write("With your asshole suddenly disappearing, " + inserter.name() + "'s dick pops out of what was once your sphincter.");
			} else {
				c.write("Your dick pops out of " + inserted.name() + " as her asshole shrinks and disappears.");
			}
			c.setStance(insert(top, bottom));
		}
	}

	public Position reverse() {
		if (top.has(Trait.strapped)) {
			return new ReverseMount(bottom, top);
		} else {
			return new AnalCowgirl(bottom, top);
		}
	}
}