package nightgames.stance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;

public class TribadismStance extends Position {
	public TribadismStance(Character top, Character bottom) {
		super(top, bottom, Stance.trib);
	}

	@Override
	public String describe() {
		return top.subjectAction("are", "is") + " holding " + bottom.nameOrPossessivePronoun() + " legs across " + top.possessivePronoun() + " chest while grinding " + top.possessivePronoun() +" soaked cunt into " + bottom.possessivePronoun() + " pussy.";
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
	}

	@Override
	public boolean kiss(Character c) {
		return true;
	}

	@Override
	public boolean inserted(Character c) {
		return false;
	}

	public String image() {
		return "trib.jpg";
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
	public Position insertRandom() {
		return new Mount(top, bottom);
	}

	@Override
	public List<BodyPart> bottomParts() {
		return Arrays.asList(bottom.body.getRandomPussy()).stream().filter(part -> part != null && part.present()).collect(Collectors.toList());
	}

	@Override
	public List<BodyPart> topParts() {
		return Arrays.asList(top.body.getRandomPussy()).stream().filter(part -> part != null && part.present()).collect(Collectors.toList());
	}
}
