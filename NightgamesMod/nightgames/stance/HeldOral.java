package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class HeldOral extends AbstractFacingStance {
    public HeldOral(Character top, Character bottom) {
        super(top, bottom, Stance.oralpin);
    }

    @Override
    public String describe(Combat c) {
        return Global.format(
                        "{self:SUBJECT-ACTION:are|is} holding {other:name-do} down with {self:possessive} face nested between {other:possessive} legs.",
                        top, bottom);
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom && c != top;
    }

    @Override
    public boolean getUp(Character c) {
        return c == top;
    }

    @Override
    public String image() {
        if (bottom.hasDick()) {
            return "oralhold_fm.jpg";
        } else if (bottom.hasPussy() && top.hasPussy()) {
            return "oralhold_ff.jpg";
        } else if (bottom.hasPussy()) {
            return "oralhold_mf.jpg";
        }
        return "err.jpg";
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c != top && target != top;
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
        return false;
    }

    @Override
    public boolean reachBottom(Character c) {
        return false;
    }

    @Override
    public boolean prone(Character c) {
        return c == bottom;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return false;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return c == top;
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
        float bonus = 2;
        bonus += self.body.getRandom("mouth").priority(self);
        return bonus;
    }

    @Override
    public Position reverse(Combat c) {
        return new Mount(bottom, top);
    }

    @Override
    public boolean faceAvailable(Character target) {
        return target == bottom;
    }

    @Override
    public double pheromoneMod(Character self) {
        if (self == bottom) {
            return 10;
        }
        return 2;
    }
    
    @Override
    public int dominance() {
        return 3;
    }

    @Override
    public int distance() {
        return 1;
    }
}
