package stance;

import combat.Combat;

import characters.Character;
import characters.Trait;

public class FlyingCowgirl extends FemdomSexStance {
	private static final long serialVersionUID = 1953597655795344915L;

	private Character top, bottom;
	
	public FlyingCowgirl (Character succ, Character target) {
		super(succ, target, Stance.flying);
		top = succ;
		bottom = target;
	}

	@Override
	public String describe() {
		return String.format("You are flying some twenty feet up in the air,"
				+ " joinned to your partner by your hips. %s on top of %s and %s %s is strangling %s %s",
				top.subjectAction("are", "is"), bottom.subject(),
				top.possessivePronoun(), top.body.getRandomPussy().describe(top),
				bottom.possessivePronoun(), bottom.body.getRandomInsertable().describe(bottom));
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
		return c==bottom;
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
		return new Mount(top, bottom);
	}

	public Position reverse() {
		if (bottom.body.getRandomWings() != null) {
			return new FlyingCarry(bottom, top);
		} else {
			return new Missionary(bottom, top);
		}
	}
}
