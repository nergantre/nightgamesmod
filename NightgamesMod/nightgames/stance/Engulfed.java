package nightgames.stance;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

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
	public boolean inserted(Character c) {
		return c.hasDick();
	}

	@Override
	public Position insertRandom() {
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

	@Override
	public List<BodyPart> topParts() {
		List<BodyPart> parts = new ArrayList<>();
		parts.addAll(top.body.get("cock"));
		parts.addAll(top.body.get("pussy"));
		parts.addAll(top.body.get("ass"));
		return parts.stream().filter(part -> part != null && part.present()).collect(Collectors.toList());
	}

	@Override
	public List<BodyPart> bottomParts() {
		List<BodyPart> parts = new ArrayList<>();
		parts.addAll(bottom.body.get("cock"));
		parts.addAll(bottom.body.get("pussy"));
		parts.addAll(bottom.body.get("ass"));
		return parts.stream().filter(part -> part != null && part.present()).collect(Collectors.toList());
	}

	@Override
	public boolean faceAvailable(Character target) {
		return target == top;
	}
	
	public double pheromoneMod (Character self) {
		return 10;
	}
}