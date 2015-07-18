package stance;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import combat.Combat;

import skills.Escape;
import skills.Nothing;
import skills.Skill;
import skills.Struggle;
import skills.Suckle;
import skills.Wait;
import characters.Character;
import characters.Emotion;
import characters.Trait;

public class Engulfed extends Position {
	public Engulfed(Character top, Character bottom) {
		super(top, bottom,Stance.engulfed);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You have engulfed " + bottom.name() + " inside your slime body, with only her face outside of you";
		}
		else{
			return top.name()+" is holding your entire body inside her slime body, with only your face outside.";
		}
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
	}

	public String image() {
		if (bottom.hasPussy()) {
			return "engulfed_f.jpg";
		} else {
			return "engulfed_m.jpg";
		}
	}

	@Override
	public boolean kiss(Character c) {
		return c==top;
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
		return c==top;
	}

	@Override
	public boolean oral(Character c) {
		return c==top;
	}

	@Override
	public boolean behind(Character c) {
		return c==top;
	}

	@Override
	public boolean front(Character c) {
		return true;
	}

	@Override
	public boolean penetration(Character c) {
		return c.hasDick();
	}

	@Override
	public boolean inserted(Character c) {
		return c.hasDick();
	}

	@Override
	public Position insert() {
		return new Neutral(top, bottom);
	}

	public void decay(Combat c){
		time++;
		bottom.weaken(null, 5);
		top.emote(Emotion.dominant, 10);
	}

	@Override
	public float priorityMod(Character self) {
		return dom(self) ? 5 : 0;
	}
}
