package stance;

import combat.Combat;

import characters.Character;
import characters.Trait;

public class FlyingCarry extends MaledomSexStance {
	private static final long serialVersionUID = 1953597655795344915L;

	private Character top, bottom;
	
	public FlyingCarry (Character succ, Character target) {
		super(succ, target, Stance.flying);
		top = succ;
		bottom = target;
	}
	
	@Override
	public String describe() {
		return String.format("You are flying some twenty feet up in the air,"
				+ " joinned to your partner by your hips. %s is on top of %s and %s %s is pumping into %s %s",
				top.subject(), bottom.subject(),
				top.possessivePronoun(), top.body.getRandomInsertable().describe(top),
				bottom.possessivePronoun(), bottom.body.getRandomPussy().describe(bottom));
	}
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
	public boolean penetration(Character c) {
		return true;
	}

	@Override
	public boolean inserted(Character c) {
		return c==top;
	}

	public boolean flying(Character c) {
		return true;
	}
	public void decay(Combat c){
		time++;
		top.weaken(null, 3);
	}
	public void checkOngoing(Combat c){
		if(top.getStamina().get()<5){
			if(top.human()){
				c.write("You're too tired to stay in the air. You plummet to the ground and "+bottom.name()+" drops on you heavily, knocking the wind out of you.");
				top.pain(c, 5);
				c.setStance(new Mount(bottom,top));
			}
			else{
				c.write(top.name()+" falls to the ground and so do you. Fortunately, her body cushions your fall, but you're not sure she appreciates that as much as you do.");
				top.pain(c, 5);
				c.setStance(new Mount(bottom,top));
			}
		} else {
			super.checkOngoing(c);
		}
	}

	@Override
	public Position insert() {
			return new StandingOver(top, bottom);
	}

	public Position reverse() {
		if (bottom.body.getRandomWings() != null) {
			return new FlyingCowgirl(bottom, top);
		} else {
			return new Cowgirl(bottom, top);
		}
	}
}
