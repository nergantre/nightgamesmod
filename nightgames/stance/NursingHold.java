package nightgames.stance;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Escape;
import nightgames.skills.Nothing;
import nightgames.skills.Skill;
import nightgames.skills.Struggle;
import nightgames.skills.Suckle;
import nightgames.skills.Wait;

public class NursingHold extends AbstractFacingStance {
	public NursingHold(Character top, Character bottom) {
		super(top, bottom,Stance.nursing);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You are cradling "+bottom.nameOrPossessivePronoun()+" head in your lap with your breasts dangling in front of " + bottom.directObject();
		}
		else{
			return top.name()+" is holding your head in her lap, with her enticing breasts right in front of your mouth.";
		}
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
	}	
	public String image() {
		return "nursing.jpg";
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
	public boolean inserted(Character c) {
		return false;
	}

	public void decay(Combat c){
		time++;
		bottom.weaken(null, 5);
		bottom.loseWillpower(c, 3);
		top.emote(Emotion.dominant, 10);
	}

	@Override
	public float priorityMod(Character self) {
		return dom(self) ? (self.has(Trait.lactating) ? 5 : 2) : 0;
	}
	@Override
	public Collection<Skill> availSkills(Character c) {
		if (c == top) {
			return Collections.emptySet();
		} else {
			Collection<Skill> avail = new HashSet<Skill>();
			avail.add(new Suckle(bottom));
			avail.add(new Escape(bottom));
			avail.add(new Struggle(bottom));
			avail.add(new Nothing(bottom));
			avail.add(new Wait(bottom));
			return avail;
		}
	}
}
