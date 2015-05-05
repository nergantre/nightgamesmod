package stance;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import skills.Anilingus;
import skills.Blowjob;
import skills.Cunnilingus;
import skills.Escape;
import skills.Nothing;
import skills.Skill;
import skills.Struggle;
import skills.Suckle;
import skills.Wait;
import combat.Combat;

import global.Global;
import characters.Character;
import characters.Emotion;

public class FaceSitting extends Position {

	public FaceSitting(Character top, Character bottom) {
		super(top, bottom,Stance.facesitting);
	}

	@Override
	public String describe() {
		return Global.capitalizeFirstLetter(top.subjectAction("are", "is")) + " sitting on " + bottom.nameOrPossessivePronoun() + " face while holding " + bottom.possessivePronoun() + " arms so " + bottom.subject() + " cannot escape";
	}

	@Override
	public boolean mobile(Character c) {
		return top==c;
	}

	@Override
	public boolean kiss(Character c) {
		return false;
	}

	@Override
	public boolean facing() {
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
		return c==bottom;
	}

	@Override
	public boolean feet(Character c) {
		return c==top;
	}

	@Override
	public boolean oral(Character c) {
		return c==bottom;
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
	public boolean inserted(Character c) {
		return false;
	}
	@Override
	public Position insert(Character dom, Character inserter) {
		return this;
	}
	public void decay(){
		time++;
		bottom.weaken(null, 5);
		top.emote(Emotion.dominant, 20);
		top.emote(Emotion.horny, 10);
	}
	@Override
	public Collection<Skill> availSkills(Character c) {
		if (c == top) {
			return Collections.emptySet();
		} else {
			Collection<Skill> avail = new HashSet<Skill>();
			avail.add(new Cunnilingus(bottom));
			avail.add(new Anilingus(bottom));
			avail.add(new Blowjob(bottom));
			avail.add(new Escape(bottom));
			avail.add(new Struggle(bottom));
			avail.add(new Nothing(bottom));
			avail.add(new Wait(bottom));
			return avail;
		}
	}
}
