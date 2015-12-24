package nightgames.stance;


import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class BehindFootjob extends AbstractBehindStance {
	public BehindFootjob(Character top, Character bottom) {
		super(top, bottom,Stance.behindfootjob);
	}

	@Override
	public String describe() {
		return Global.format("{self:SUBJECT-ACTION:are|is} holding {other:name-do} from behind with {self:possessive} legs wrapped around {other:direct-object}", top, bottom);
	}

	@Override
	public int pinDifficulty(Combat c, Character self) {
		return 6;
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
	}

	public String image() {
		if (bottom.hasDick()) {
			return "behind_footjob.jpg";
		} else {
			return "heelgrind.jpg";
		}
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
		return c==top;
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
		return c==top;
	}

	@Override
	public boolean oral(Character c) {
		return false;
	}

	@Override
	public boolean behind(Character c) {
		return c==top;
	}

	@Override
	public boolean inserted(Character c) {
		return false;
	}

	@Override
	public float priorityMod(Character self) {
		return getSubDomBonus(self, 4.0f);
	}

	public Position reverse(Combat c) {
		c.write(bottom, Global.format("{self:SUBJECT-ACTION:summon what little willpower you have left and grab|grabs} {other:name-possessive} feet and pull them off {self:name-possessive} crotch. Taking advantage of the momentum, {self:subject} push {other:direct-object} back with {self:name-possessive} body and hold {other:direct-object} down while sitting on top of {other:direct-object}.", bottom, top));
		return new ReverseMount(bottom, top);
	}

	public double pheromoneMod (Character self) {
		return 1.5;
	}
}
