package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class Mount extends AbstractFacingStance {

    public Mount(Character top, Character bottom) {
        super(top, bottom, Stance.mount);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You're on top of " + bottom.name() + ".";
        } else {
            return String.format("%s straddling %s, with %s enticing breasts right in front of %s.",
                            top.subjectAction("are", "is"), bottom.nameDirectObject(),
                            top.possessivePronoun(), bottom.directObject());
        }
    }

    @Override
    public String image() {
        if (top.hasPussy() && bottom.hasPussy()) {
            return "mount_ff.jpg";
        } else if (bottom.hasPussy()) {
            return "mount_m.jpg";
        } else {
            return "mount_f.jpg";
        }
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return true;
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
        return c != bottom;
    }

    @Override
    public boolean prone(Character c) {
        return c == bottom;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return target == bottom && c != top && c != bottom;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return target == bottom && c != top && c != bottom;
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
        return getSubDomBonus(self, 4.0f);
    }

    @Override
    public double pheromoneMod(Character self) {
        return 3;
    }
    
    @Override
    public int dominance() {
        return 2;
    }

    @Override
    public int distance() {
        return 1;
    }
}
