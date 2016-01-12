package nightgames.stance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class AnalCowgirl extends AnalSexStance {

	public AnalCowgirl(Character top, Character bottom) {
		super(top, bottom, Stance.anal);
	}

	@Override
	public String describe() {
		if (top.human()) {
			return String.format("You're sitting on top of %s with your ass squeezing her cock.",
					bottom.nameDirectObject());
		} else {
			return "You're flat on your back with your cock buried inside " + top.nameOrPossessivePronoun() + " ass.";
		}
	}

	@Override
	public boolean mobile(Character c) {
		return c == top;
	}

	@Override
	public boolean kiss(Character c) {
		return false;
	}

	@Override
	public boolean dom(Character c) {
		return c == top;
	}

	@Override
	public boolean sub(Character c) {
		return c == bottom;
	}

	@Override
	public boolean reachTop(Character c) {
		return true;
	}

	@Override
	public boolean reachBottom(Character c) {
		return c == top;
	}

	@Override
	public boolean prone(Character c) {
		return c == bottom;
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
		return c == bottom;
	}

	@Override
	public Position insertRandom() {
		return new Mount(top, bottom);
	}

	@Override
	public String image() {
		return "anal_cowgirl.jpg";
	}

	@Override
	public void checkOngoing(Combat c) {
		Character inserter = inserted(top) ? top : bottom;
		Character inserted = inserted(top) ? bottom : top;

		if (!inserter.hasInsertable()) {
			if (inserter.human()) {
				c.write("With " + inserter.possessivePronoun()
						+ " pole gone, you groan in frustration and cease your merciless movements.");
			} else {
				c.write(inserted.name() + " groans with frustration with the sudden disappearance of your pole.");
			}
			c.setStance(insertRandom());
		}
		if (inserted.body.getRandom("ass") == null) {
			if (inserted.human()) {
				c.write("With your asshole suddenly disappearing, you can't continue riding " + inserter.name()
						+ " anymore.");
			} else {
				c.write(inserted.name() + " groans with frustration with the sudden disappearance of her asshole.");
			}
			c.setStance(insertRandom());
		}
	}

	@Override
	public boolean anallyPenetrated(Character self) {
		return self == top;
	}

	@Override
	public List<BodyPart> topParts() {
		return Arrays.asList(top.body.getRandomAss()).stream().filter(part -> part != null && part.present())
				.collect(Collectors.toList());
	}

	@Override
	public List<BodyPart> bottomParts() {
		return Arrays.asList(bottom.body.getRandomInsertable()).stream().filter(part -> part != null && part.present())
				.collect(Collectors.toList());
	}
}
