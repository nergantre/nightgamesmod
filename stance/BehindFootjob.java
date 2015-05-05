package stance;


import global.Global;
import characters.Character;
import characters.Trait;

public class BehindFootjob extends Position {
	public BehindFootjob(Character top, Character bottom) {
		super(top, bottom,Stance.behindfootjob);
	}

	@Override
	public String describe() {
		return Global.format("{self:SUBJECT-ACTION:are|is} holding {other:name-do} from behind with {self:possessive} legs wrapped around {other:direct-object}", top, bottom);
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
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
	public boolean penetration(Character c) {
		return false;
	}

	@Override
	public boolean inserted(Character c) {
		return false;
	}

	@Override
	public Position insert(Character dom, Character inserter) {
		return new Doggy(top, bottom);
	}

	@Override
	public float priorityMod(Character self) {
		return (dom(self) ? 4.0f : 0);
	}

	public Position reverse() {
		return new Mount(bottom, top);
	}
}
