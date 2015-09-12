package nightgames.stance;
import java.util.Collection;
import java.util.Collections;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.skills.Skill;
import nightgames.status.Stsflag;

public abstract class Position implements Cloneable{
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
	public int pinDifficulty(Combat c, Character self) {
		return 4;
	}
	public int escape(Combat c, Character self) {
		if (sub(self) && !mobile(self)) {
			return -pinDifficulty(c, self) * (Math.max(-5, 10 - time));
		}
		return 0;
	}
	public void struggle() {
		time += 2;
	}
	public void decay(Combat c){
		time++;
	}
	public void checkOngoing(Combat c){
		return;
	}
	public float getSubDomBonus(Character self, float bonus) {
		if (!self.human()) {
			if (self.has(Trait.submissive) && sub(self)) {
				return bonus;
			}
			if (!self.has(Trait.submissive) && dom(self)) {
				return bonus;
			}
		}
		return 0;
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
	public boolean getUp(Character c) {
		return mobile(c);
	}
	public boolean front(Character c) {
		return !behind(c);
	}
	public abstract boolean inserted(Character c);
	public abstract String image();

	public boolean inserted() {
		return inserted(top) || inserted(bottom);
	}
	
	public Position insert(Character pitcher, Character dom) {
		return this;
	}

	public Position insertRandom() {
		return insertRandomDom(top);
	}

	public Collection<Skill> availSkills(Character c) {
		return Collections.emptySet();
	}

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
		return (self.crotchAvailable()||(self.has(Trait.strapped)&&target.hasPussy()))
				&& target.crotchAvailable()
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
	public Position reverse(Combat c) {
		Position newStance;
		try {
			newStance = this.clone();
		} catch (CloneNotSupportedException e) {
			newStance = this;
		}
		Character nbot = this.top;
		Character ntop = this.bottom;
		newStance.bottom = nbot;
		newStance.top = ntop;
		return newStance;
	}
	public boolean anallyPenetrated() {
		return anallyPenetrated(top) || anallyPenetrated(bottom);
	}

	public boolean anallyPenetrated(Character self) {
		BodyPart part = partFor(self);
		return self.is(Stsflag.pegged) || (part != null && part.isType("ass"));
	}

	public Position insertRandomDom(Character target) {
		return this;
	}

	public Character getOther(Character c) {
		if (c == top) {
			return bottom;
		} else {
			return top;
		}
	}
	public boolean paizuri(Character self) {
		return oral(self);
	}
	
	public BodyPart topPart() {
		if (inserted())
			throw new UnsupportedOperationException("Attempted to get topPart in position " + getClass().getSimpleName()
					+ ", but that position does not override the appropriate method.");
		return Body.nonePart;
	}

	public BodyPart bottomPart() {
		if (inserted())
			throw new UnsupportedOperationException("Attempted to get bottomPart in position "
					+ getClass().getSimpleName() + ", but that position does not override the appropriate method.");
		return Body.nonePart;
	}
	
	public BodyPart partFor(Character c) {
		return c.equals(top) ? topPart() : bottomPart();
	}
	public boolean vaginallyPenetrated() {
		return vaginallyPenetrated(top) || vaginallyPenetrated(bottom);
	}
	public boolean penetrated(Character c) { 
		return vaginallyPenetrated(c) || anallyPenetrated(c);
	}
	public boolean vaginallyPenetrated(Character c) {
		BodyPart part = partFor(c);
		return (part != null && part.isType("pussy")) || c.is(Stsflag.fucked);
	}
	public boolean havingSexOtherNoStrapped(Character c) {
		Character other = getOther(c);
		return penetratedBy(other, c) || (penetratedBy(c, other) && !other.has(Trait.strapped));
	}
	public boolean havingSexNoStrapped() {
		return (penetratedBy(top, bottom) && !bottom.has(Trait.strapped)) || (penetratedBy(bottom, top) && !top.has(Trait.strapped));
	}
	public boolean havingSex() {
		return penetratedBy(top, bottom) || penetratedBy(bottom, top);
	}
	public boolean penetratedBy(Character inserted, Character inserter) {
		return vaginallyPenetratedBy(inserted, inserter) || anallyPenetratedBy(inserted, inserter);
	}
	public boolean vaginallyPenetratedBy(Character self, Character other) {
		if (other != getOther(self)) { return false; }
		BodyPart part = partFor(self);
		return part != null && part.isType("pussy");
	}
	public boolean anallyPenetratedBy(Character self, Character other) {
		if (other != getOther(self)) { return false; }
		BodyPart part = partFor(self);
		return part != null && part.isType("ass");
	}
	public boolean connected() {
		return anallyPenetrated() || vaginallyPenetrated() || inserted();
	}
}
