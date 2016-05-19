package nightgames.stance;

import nightgames.characters.Character;

public class Kneeling extends AbstractFacingStance {

    public Kneeling(Character top, Character bottom) {
        super(top, bottom, Stance.kneeling);

    }

    @Override
    public String describe() {
        if (top.human()) {
            return "You are standing over " + bottom.name() + ", who is kneeling before you.";
        } else {
            return "You are kneeling on the ground, while " + top.name() + " stands over you.";
        }
    }

    @Override
    public boolean mobile(Character c) {
        return true;
    }

    @Override
    public boolean kiss(Character c) {
        return false;
    }

    @Override
    public String image() {
        if (bottom.hasPussy() || !bottom.hasDick()) {
            return "kneeling_f.jpg";
        } else {
            return "kneeling_m.jpg";
        }
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
        return c == top;
    }

    @Override
    public boolean reachBottom(Character c) {
        return true;
    }

    @Override
    public boolean prone(Character c) {
        return c == bottom;
    }

    @Override
    public boolean feet(Character c) {
        return c == top;
    }

    @Override
    public boolean oral(Character c) {
        return c == bottom;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public boolean inserted(Character c) {
        return false;
    }

    @Override
    public float priorityMod(Character self) {
        return getSubDomBonus(self, 2.0f);
    }

    @Override
    public double pheromoneMod(Character self) {
        return 1.5;
    }
}
