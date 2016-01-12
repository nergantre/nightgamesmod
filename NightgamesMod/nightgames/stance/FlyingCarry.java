package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class FlyingCarry extends MaledomSexStance {

	private Character top, bottom;

	public FlyingCarry(Character succ, Character target) {
		super(succ, target, Stance.flying);
		top = succ;
		bottom = target;
	}

	@Override
	public String describe() {
		return String.format(
				"You are flying some twenty feet up in the air,"
						+ " joinned to your partner by your hips. %s is on top of %s and %s %s is pumping into %s %s",
				top.subject(), bottom.subject(), top.possessivePronoun(), top.body.getRandomInsertable().describe(top),
				bottom.possessivePronoun(), bottom.body.getRandomPussy().describe(bottom));
	}

	@Override
	public String image() {
		return "flying.jpg";
	}

	@Override
	public boolean mobile(Character c) {
		return top.equals(c);
	}

	@Override
	public boolean kiss(Character c) {
		return true;
	}

	@Override
	public boolean dom(Character c) {
		return top.equals(c);
	}

	@Override
	public boolean sub(Character c) {
		return !top.equals(c);
	}

	@Override
	public boolean reachTop(Character c) {
		return true;
	}

	@Override
	public boolean reachBottom(Character c) {
		return top.equals(c);
	}

	@Override
	public boolean prone(Character c) {
		return !top.equals(c);
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
		return c == top;
	}

	public boolean flying(Character c) {
		return true;
	}

	@Override
	public void decay(Combat c) {
		time++;
		top.weaken(null, 3);
	}

	@Override
	public void checkOngoing(Combat c) {
		if (top.getStamina().get() < 5) {
			if (top.human()) {
				c.write("You're too tired to stay in the air. You plummet to the ground and " + bottom.name()
						+ " drops on you heavily, knocking the wind out of you.");
				top.pain(c, 50);
				c.setStance(new Mount(bottom, top));
			} else {
				c.write(top.name()
						+ " falls to the ground and so do you. Fortunately, her body cushions your fall, but you're not sure she appreciates that as much as you do.");
				top.pain(c, 50);
				c.setStance(new Mount(bottom, top));
			}
		} else {
			super.checkOngoing(c);
		}
	}

	@Override
	public Position insertRandom() {
		return new StandingOver(top, bottom);
	}

	@Override
	public Position reverse(Combat c) {
		if (bottom.body.getRandomWings() != null) {
			c.write(bottom,
					Global.format(
							"In a desperate gamble for dominance, {self:subject} rides {other:name-do} wildly, making {other:direct-object} gasp and breaking {other:possessive} concentration. Shaking off {other:possessive} strong arms, {self:subject} starts flying on {self:possessive} own and starts riding {other:direct-object} with more control in the air.",
							bottom, top));
			return new FlyingCowgirl(bottom, top);
		} else {
			c.write("Weakened by {self:possessive} squirming, {other:SUBJECT-ACTION:fall|falls} to the ground and so {self:action:do|does} {self:name-do}. Fortunately, {other:possessive} body cushions {self:possessive} fall, but you're not sure {self:action:she appreciates that as much as you do|you appreciate that as much as her}. While {other:subject-action:are|is} dazed, {self:subject-action:mount|mounts} {other:direct-object} and {self:action:start|starts} riding {other:direct-object} in a cowgirl position.");
			top.pain(c, 50);
			return new Cowgirl(bottom, top);
		}
	}
}
