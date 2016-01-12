package nightgames.stance;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Jumped extends FemdomSexStance {
	public Jumped(Character top, Character bottom) {
		super(top, bottom,Stance.standing);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You are clinging to "+bottom.nameOrPossessivePronoun() + " arms while her dick is buried deep in your pussy";
		}
		else{
			return top.name()+" is clinging to your shoulders and gripping your waist with her thighs while she uses the leverage to ride you.";
		}
	}
	public String image() {
		return "standing.jpg";
	}
	@Override
	public boolean mobile(Character c) {
		return false;
	}

	@Override
	public boolean kiss(Character c) {
		return true;
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
		return false;
	}

	@Override
	public boolean reachBottom(Character c) {
		return false;
	}

	@Override
	public boolean prone(Character c) {
		return false;
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
		return new Neutral(top,bottom);
	}
	public void decay(Combat c){
		time++;
		top.weaken(null, 2);
	}

	public void checkOngoing(Combat c){
		if(bottom.getStamina().get()<2 && !top.has(Trait.petite)){
			if(bottom.human()){
				c.write("Your legs give out and you fall on the floor. "+top.name()+" lands heavily on your lap.");
				c.setStance(new Cowgirl(top, bottom));
			}
			else{
				c.write(bottom.name()+" loses her balance and falls, pulling you down on top of her.");
				c.setStance(new Cowgirl(top,bottom));
			}
		} else {
			super.checkOngoing(c);
		}
	}

	public Position reverse(Combat c) {
		c.write(bottom, Global.format("{self:SUBJECT-ACTION:pinch|pinches} {other:possessive} clitoris with {self:possessive} hands as {other:subject-action:try|tries} to ride {self:direct-object}. " +
				"While {other:subject-action:yelp|yelps} with surprise, {self:subject-action:take|takes} the chance to push {other:direct-object} against a wall and fuck her in a standing position.", bottom, top));
		return new Standing(bottom, top);
	}
}
