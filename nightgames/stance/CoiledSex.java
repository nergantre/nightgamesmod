package nightgames.stance;


import nightgames.characters.Character;
import nightgames.characters.body.AnalPussyPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;

public class CoiledSex extends FemdomSexStance {

	public CoiledSex(Character top, Character bottom) {
		super(top, bottom,Stance.flowertrap);
	}

	public int pinDifficulty(Combat c, Character self) {
		return 8;
	}

	@Override
	public String describe() {
		if(top.human()){
			return "Your limbs are coiled around "+bottom.nameOrPossessivePronoun() + " body and " + bottom.possessivePronoun() + " cock is inside you.";
		} else {
			return "You're on top of " + top.nameDirectObject() + " with your cock trapped in her pussy and your face smothered in her cleavage.";
		}
	}

	public String image() {
		return "coiledsex.png";
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

	@Override
	public Position insertRandom() {
		return new Mount(top,bottom);
	}

	public Position reverse() {
		return this;
	}

	@Override
	public BodyPart topPart() {
		return top.body.getRandomPussy();
	}
	
	@Override
	public BodyPart bottomPart() {
		return bottom.body.getRandomInsertable();
	}
}
