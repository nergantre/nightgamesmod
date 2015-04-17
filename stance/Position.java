package stance;
import characters.Character;
import characters.Trait;

import java.io.Serializable;

import combat.Combat;



public abstract class Position implements Serializable, Cloneable{
	public Character top;
	public Character bottom;
	public int time;
	public Stance en;
	public Position(Character top, Character bottom, Stance stance){
		this.top=top;
		this.bottom=bottom;
		this.en=stance;
		time=0;
	}
	public void decay(){
		time++;
	}
	public void checkOngoing(Combat c){
		return;
	}
	public abstract String describe();
	public abstract boolean mobile(Character c);
	public abstract boolean kiss(Character c);
	public abstract boolean dom(Character c);
	public abstract boolean sub(Character c);
	public abstract boolean reachTop(Character c);
	public abstract boolean reachBottom(Character c);
	public abstract boolean prone(Character c);
	public abstract boolean feet(Character c);
	public abstract boolean oral(Character c);
	public abstract boolean behind(Character c);
	public abstract boolean penetration(Character c);
	public abstract boolean inserted(Character c);
	public abstract Position insert(Character dom, Character inserter);

	public boolean canthrust(Character c) {
		return this.dom(c) || c.has(Trait.powerfulhips);
	}

	public boolean facing() {
		return !this.behind(top) && !this.behind(bottom);
	}
	
	public float priorityMod(Character self) {
		return 0;
	}

	public boolean fuckable(Character self) {
		Character target;

		if (self == top) { target = bottom; } else { target = top; }
		return (self.pantsless()||(self.has(Trait.strapped)&&target.hasPussy()))
				&& target.pantsless()
				&& mobile(self)
				&& !mobile(target)
				&& (((self.hasDick() || self.has(Trait.strapped)) && !behind(target)) || !behind(self))
				&& self.canAct();
	}

	public Stance enumerate(){
		return this.en;
	}
	public Position clone() throws CloneNotSupportedException {
	    return (Position) super.clone();
	}
	public Character other(Character character) {
		if (character.name().equals(top.name()))
			return bottom;
		else if (character.name().equals(bottom.name()))
			return top;
		return null;
	}
}
